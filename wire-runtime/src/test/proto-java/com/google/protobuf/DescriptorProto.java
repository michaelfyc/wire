// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: ../wire-runtime/src/test/proto/google/protobuf/descriptor.proto at 79:1
package com.google.protobuf;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireField;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import okio.ByteString;

/**
 * Describes a message type.
 */
public final class DescriptorProto extends Message<DescriptorProto> {
  public static final ProtoAdapter<DescriptorProto> ADAPTER = ProtoAdapter.newMessageAdapter(DescriptorProto.class);

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_NAME = "";

  public static final String DEFAULT_DOC = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String name;

  /**
   * Doc string for generated code.
   */
  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String doc;

  @WireField(
      tag = 2,
      adapter = "com.google.protobuf.FieldDescriptorProto#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<FieldDescriptorProto> field;

  @WireField(
      tag = 6,
      adapter = "com.google.protobuf.FieldDescriptorProto#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<FieldDescriptorProto> extension;

  @WireField(
      tag = 3,
      adapter = "com.google.protobuf.DescriptorProto#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<DescriptorProto> nested_type;

  @WireField(
      tag = 4,
      adapter = "com.google.protobuf.EnumDescriptorProto#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<EnumDescriptorProto> enum_type;

  @WireField(
      tag = 5,
      adapter = "com.google.protobuf.DescriptorProto$ExtensionRange#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<ExtensionRange> extension_range;

  @WireField(
      tag = 7,
      adapter = "com.google.protobuf.MessageOptions#ADAPTER"
  )
  public final MessageOptions options;

  public DescriptorProto(String name, String doc, List<FieldDescriptorProto> field, List<FieldDescriptorProto> extension, List<DescriptorProto> nested_type, List<EnumDescriptorProto> enum_type, List<ExtensionRange> extension_range, MessageOptions options) {
    this(name, doc, field, extension, nested_type, enum_type, extension_range, options, ByteString.EMPTY);
  }

  public DescriptorProto(String name, String doc, List<FieldDescriptorProto> field, List<FieldDescriptorProto> extension, List<DescriptorProto> nested_type, List<EnumDescriptorProto> enum_type, List<ExtensionRange> extension_range, MessageOptions options, ByteString unknownFields) {
    super(unknownFields);
    this.name = name;
    this.doc = doc;
    this.field = immutableCopyOf(field);
    this.extension = immutableCopyOf(extension);
    this.nested_type = immutableCopyOf(nested_type);
    this.enum_type = immutableCopyOf(enum_type);
    this.extension_range = immutableCopyOf(extension_range);
    this.options = options;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof DescriptorProto)) return false;
    DescriptorProto o = (DescriptorProto) other;
    return equals(unknownFields(), o.unknownFields())
        && equals(name, o.name)
        && equals(doc, o.doc)
        && equals(field, o.field)
        && equals(extension, o.extension)
        && equals(nested_type, o.nested_type)
        && equals(enum_type, o.enum_type)
        && equals(extension_range, o.extension_range)
        && equals(options, o.options);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (name != null ? name.hashCode() : 0);
      result = result * 37 + (doc != null ? doc.hashCode() : 0);
      result = result * 37 + (field != null ? field.hashCode() : 1);
      result = result * 37 + (extension != null ? extension.hashCode() : 1);
      result = result * 37 + (nested_type != null ? nested_type.hashCode() : 1);
      result = result * 37 + (enum_type != null ? enum_type.hashCode() : 1);
      result = result * 37 + (extension_range != null ? extension_range.hashCode() : 1);
      result = result * 37 + (options != null ? options.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  public static final class Builder extends com.squareup.wire.Message.Builder<DescriptorProto, Builder> {
    public String name;

    public String doc;

    public List<FieldDescriptorProto> field;

    public List<FieldDescriptorProto> extension;

    public List<DescriptorProto> nested_type;

    public List<EnumDescriptorProto> enum_type;

    public List<ExtensionRange> extension_range;

    public MessageOptions options;

    public Builder() {
      field = newMutableList();
      extension = newMutableList();
      nested_type = newMutableList();
      enum_type = newMutableList();
      extension_range = newMutableList();
    }

    public Builder(DescriptorProto message) {
      super(message);
      if (message == null) return;
      this.name = message.name;
      this.doc = message.doc;
      this.field = copyOf(message.field);
      this.extension = copyOf(message.extension);
      this.nested_type = copyOf(message.nested_type);
      this.enum_type = copyOf(message.enum_type);
      this.extension_range = copyOf(message.extension_range);
      this.options = message.options;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * Doc string for generated code.
     */
    public Builder doc(String doc) {
      this.doc = doc;
      return this;
    }

    public Builder field(List<FieldDescriptorProto> field) {
      checkElementsNotNull(field);
      this.field = field;
      return this;
    }

    public Builder extension(List<FieldDescriptorProto> extension) {
      checkElementsNotNull(extension);
      this.extension = extension;
      return this;
    }

    public Builder nested_type(List<DescriptorProto> nested_type) {
      checkElementsNotNull(nested_type);
      this.nested_type = nested_type;
      return this;
    }

    public Builder enum_type(List<EnumDescriptorProto> enum_type) {
      checkElementsNotNull(enum_type);
      this.enum_type = enum_type;
      return this;
    }

    public Builder extension_range(List<ExtensionRange> extension_range) {
      checkElementsNotNull(extension_range);
      this.extension_range = extension_range;
      return this;
    }

    public Builder options(MessageOptions options) {
      this.options = options;
      return this;
    }

    @Override
    public DescriptorProto build() {
      return new DescriptorProto(name, doc, field, extension, nested_type, enum_type, extension_range, options, buildUnknownFields());
    }
  }

  public static final class ExtensionRange extends Message<ExtensionRange> {
    public static final ProtoAdapter<ExtensionRange> ADAPTER = ProtoAdapter.newMessageAdapter(ExtensionRange.class);

    private static final long serialVersionUID = 0L;

    public static final Integer DEFAULT_START = 0;

    public static final Integer DEFAULT_END = 0;

    @WireField(
        tag = 1,
        adapter = "com.squareup.wire.ProtoAdapter#INT32"
    )
    public final Integer start;

    @WireField(
        tag = 2,
        adapter = "com.squareup.wire.ProtoAdapter#INT32"
    )
    public final Integer end;

    public ExtensionRange(Integer start, Integer end) {
      this(start, end, ByteString.EMPTY);
    }

    public ExtensionRange(Integer start, Integer end, ByteString unknownFields) {
      super(unknownFields);
      this.start = start;
      this.end = end;
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof ExtensionRange)) return false;
      ExtensionRange o = (ExtensionRange) other;
      return equals(unknownFields(), o.unknownFields())
          && equals(start, o.start)
          && equals(end, o.end);
    }

    @Override
    public int hashCode() {
      int result = super.hashCode;
      if (result == 0) {
        result = unknownFields().hashCode();
        result = result * 37 + (start != null ? start.hashCode() : 0);
        result = result * 37 + (end != null ? end.hashCode() : 0);
        super.hashCode = result;
      }
      return result;
    }

    public static final class Builder extends com.squareup.wire.Message.Builder<ExtensionRange, ExtensionRange.Builder> {
      public Integer start;

      public Integer end;

      public Builder() {
      }

      public Builder(ExtensionRange message) {
        super(message);
        if (message == null) return;
        this.start = message.start;
        this.end = message.end;
      }

      public Builder start(Integer start) {
        this.start = start;
        return this;
      }

      public Builder end(Integer end) {
        this.end = end;
        return this;
      }

      @Override
      public ExtensionRange build() {
        return new ExtensionRange(start, end, buildUnknownFields());
      }
    }
  }
}
