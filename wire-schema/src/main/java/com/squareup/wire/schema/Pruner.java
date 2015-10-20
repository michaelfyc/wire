/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.wire.schema;

import com.google.common.collect.ImmutableList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * Creates a new schema that contains only the types selected by an identifier set, including their
 * transitive dependencies.
 */
final class Pruner {
  final Schema schema;
  final IdentifierSet identifierSet;
  final MarkSet marks;

  /**
   * {@link ProtoType types} and {@link ProtoMember members} whose immediate dependencies have not
   * yet been visited.
   */
  final Deque<Object> queue;

  public Pruner(Schema schema, IdentifierSet identifierSet) {
    this.schema = schema;
    this.identifierSet = identifierSet;
    this.marks = new MarkSet(identifierSet);
    this.queue = new ArrayDeque<>();
  }

  public Schema prune() {
    markRoots();
    markReachable();

    ImmutableList.Builder<ProtoFile> retained = ImmutableList.builder();
    for (ProtoFile protoFile : schema.protoFiles()) {
      retained.add(protoFile.retainAll(schema, marks));
    }

    return new Schema(retained.build());
  }

  private void markRoots() {
    for (ProtoFile protoFile : schema.protoFiles()) {
      markRoots(protoFile);
    }
  }

  private void markRoots(ProtoFile protoFile) {
    for (Type type : protoFile.types()) {
      markRoots(type);
    }
    for (Service service : protoFile.services()) {
      markRoots(service);
    }
  }

  private void markRoots(Type type) {
    ProtoType protoType = type.name();
    if (identifierSet.includes(protoType)) {
      marks.root(protoType);
      queue.add(protoType);
    } else {
      if (type instanceof MessageType) {
        for (Field field : ((MessageType) type).fieldsAndOneOfFields()) {
          markRoots(ProtoMember.get(protoType, field.name()));
        }
      } else if (type instanceof EnumType) {
        for (EnumConstant enumConstant : ((EnumType) type).constants()) {
          markRoots(ProtoMember.get(protoType, enumConstant.name()));
        }
      } else {
        throw new AssertionError();
      }
    }

    for (Type nested : type.nestedTypes()) {
      markRoots(nested);
    }
  }

  private void markRoots(Service service) {
    ProtoType protoType = service.type();
    if (identifierSet.includes(protoType)) {
      marks.root(protoType);
      queue.add(protoType);
    } else {
      for (Rpc rpc : service.rpcs()) {
        markRoots(ProtoMember.get(protoType, rpc.name()));
      }
    }
  }

  private void markRoots(ProtoMember protoMember) {
    if (identifierSet.includes(protoMember)) {
      marks.root(protoMember);
      queue.add(protoMember);
    }
  }

  private void markReachable() {
    // Mark everything reachable by what's enqueued, queueing new things as we go.
    for (Object root; (root = queue.poll()) != null;) {
      if (root instanceof ProtoMember) {
        ProtoMember protoMember = (ProtoMember) root;
        mark(protoMember.type());
        String member = ((ProtoMember) root).member();
        Type type = schema.getType(protoMember.type());
        if (type instanceof MessageType) {
          Field field = ((MessageType) type).field(member);
          if (field == null) {
            field = ((MessageType) type).extensionField(member);
          }
          if (field != null) {
            markField(field);
            continue;
          }
        } else if (type instanceof EnumType) {
          EnumConstant constant = ((EnumType) type).constant(member);
          if (constant != null) {
            markOptions(constant.options());
            continue;
          }
        }

        Service service = schema.getService(protoMember.type());
        if (service != null) {
          Rpc rpc = service.rpc(member);
          if (rpc != null) {
            markRpc(rpc);
            continue;
          }
        }

        throw new IllegalArgumentException("Unexpected member: " + root);

      } else if (root instanceof ProtoType) {
        ProtoType protoType = (ProtoType) root;
        if (protoType.isScalar()) {
          continue; // Skip scalar types.
        }

        Type type = schema.getType(protoType);
        if (type != null) {
          markType(type);
          continue;
        }

        Service service = schema.getService(protoType);
        if (service != null) {
          markService(service);
          continue;
        }

        throw new IllegalArgumentException("Unexpected type: " + root);

      } else {
        throw new AssertionError();
      }
    }
  }

  private void mark(ProtoType type) {
    if (marks.mark(type)) {
      queue.add(type); // The transitive dependencies of this type must be visited.
    }
  }

  private void mark(ProtoMember protoMember) {
    if (marks.mark(protoMember)) {
      queue.add(protoMember); // The transitive dependencies of this member must be visited.
    }
  }

  private void markType(Type type) {
    markOptions(type.options());

    String enclosingTypeOrPackage = type.name().enclosingTypeOrPackage();
    Type enclosingType = schema.getType(enclosingTypeOrPackage);
    if (enclosingType != null) {
      mark(enclosingType.name());
    }

    if (marks.containsAllMembers(type.name())) {
      if (type instanceof MessageType) {
        markMessage((MessageType) type);
      } else if (type instanceof EnumType) {
        markEnum((EnumType) type);
      }
    }
  }

  private void markMessage(MessageType message) {
    markFields(message.fields());
    for (OneOf oneOf : message.oneOfs()) {
      markFields(oneOf.fields());
    }
  }

  private void markEnum(EnumType wireEnum) {
    markOptions(wireEnum.options());
    if (marks.containsAllMembers(wireEnum.name())) {
      for (EnumConstant constant : wireEnum.constants()) {
        markOptions(constant.options());
      }
    }
  }

  private void markFields(ImmutableList<Field> fields) {
    for (Field field : fields) {
      markField(field);
    }
  }

  private void markField(Field field) {
    markOptions(field.options());
    mark(field.type());
  }

  private void markOptions(Options options) {
    for (Map.Entry<ProtoType, ProtoMember> entry : options.fields().entries()) {
      mark(entry.getValue());
    }
  }

  private void markService(Service service) {
    markOptions(service.options());
    if (marks.containsAllMembers(service.type())) {
      for (Rpc rpc : service.rpcs()) {
        markRpc(rpc);
      }
    }
  }

  private void markRpc(Rpc rpc) {
    markOptions(rpc.options());
    mark(rpc.requestType());
    mark(rpc.responseType());
  }
}
