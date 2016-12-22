// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/socket_response.proto

package com.lawsofnature.gamecenter.common.domain;

public final class SocketResponse {
  private SocketResponse() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface GameStartResponseOrBuilder extends
      // @@protoc_insertion_point(interface_extends:GameStartResponse)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string ti = 1;</code>
     */
    String getTi();
    /**
     * <code>optional string ti = 1;</code>
     */
    com.google.protobuf.ByteString
        getTiBytes();

    /**
     * <code>repeated int32 cards = 2;</code>
     */
    java.util.List<Integer> getCardsList();
    /**
     * <code>repeated int32 cards = 2;</code>
     */
    int getCardsCount();
    /**
     * <code>repeated int32 cards = 2;</code>
     */
    int getCards(int index);

    /**
     * <code>repeated int32 dzCards = 3;</code>
     */
    java.util.List<Integer> getDzCardsList();
    /**
     * <code>repeated int32 dzCards = 3;</code>
     */
    int getDzCardsCount();
    /**
     * <code>repeated int32 dzCards = 3;</code>
     */
    int getDzCards(int index);

    /**
     * <code>optional bool isChooseDz = 4;</code>
     */
    boolean getIsChooseDz();

    /**
     * <code>optional string previousUsername = 5;</code>
     */
    String getPreviousUsername();
    /**
     * <code>optional string previousUsername = 5;</code>
     */
    com.google.protobuf.ByteString
        getPreviousUsernameBytes();

    /**
     * <code>optional string nextUsername = 6;</code>
     */
    String getNextUsername();
    /**
     * <code>optional string nextUsername = 6;</code>
     */
    com.google.protobuf.ByteString
        getNextUsernameBytes();
  }
  /**
   * Protobuf type {@code GameStartResponse}
   */
  public  static final class GameStartResponse extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:GameStartResponse)
      GameStartResponseOrBuilder {
    // Use GameStartResponse.newBuilder() to construct.
    private GameStartResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private GameStartResponse() {
      ti_ = "";
      cards_ = java.util.Collections.emptyList();
      dzCards_ = java.util.Collections.emptyList();
      isChooseDz_ = false;
      previousUsername_ = "";
      nextUsername_ = "";
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private GameStartResponse(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              String s = input.readStringRequireUtf8();

              ti_ = s;
              break;
            }
            case 16: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                cards_ = new java.util.ArrayList<Integer>();
                mutable_bitField0_ |= 0x00000002;
              }
              cards_.add(input.readInt32());
              break;
            }
            case 18: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002) && input.getBytesUntilLimit() > 0) {
                cards_ = new java.util.ArrayList<Integer>();
                mutable_bitField0_ |= 0x00000002;
              }
              while (input.getBytesUntilLimit() > 0) {
                cards_.add(input.readInt32());
              }
              input.popLimit(limit);
              break;
            }
            case 24: {
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
                dzCards_ = new java.util.ArrayList<Integer>();
                mutable_bitField0_ |= 0x00000004;
              }
              dzCards_.add(input.readInt32());
              break;
            }
            case 26: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004) && input.getBytesUntilLimit() > 0) {
                dzCards_ = new java.util.ArrayList<Integer>();
                mutable_bitField0_ |= 0x00000004;
              }
              while (input.getBytesUntilLimit() > 0) {
                dzCards_.add(input.readInt32());
              }
              input.popLimit(limit);
              break;
            }
            case 32: {

              isChooseDz_ = input.readBool();
              break;
            }
            case 42: {
              String s = input.readStringRequireUtf8();

              previousUsername_ = s;
              break;
            }
            case 50: {
              String s = input.readStringRequireUtf8();

              nextUsername_ = s;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          cards_ = java.util.Collections.unmodifiableList(cards_);
        }
        if (((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
          dzCards_ = java.util.Collections.unmodifiableList(dzCards_);
        }
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return SocketResponse.internal_static_GameStartResponse_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return SocketResponse.internal_static_GameStartResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              GameStartResponse.class, Builder.class);
    }

    private int bitField0_;
    public static final int TI_FIELD_NUMBER = 1;
    private volatile Object ti_;
    /**
     * <code>optional string ti = 1;</code>
     */
    public String getTi() {
      Object ref = ti_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        ti_ = s;
        return s;
      }
    }
    /**
     * <code>optional string ti = 1;</code>
     */
    public com.google.protobuf.ByteString
        getTiBytes() {
      Object ref = ti_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        ti_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int CARDS_FIELD_NUMBER = 2;
    private java.util.List<Integer> cards_;
    /**
     * <code>repeated int32 cards = 2;</code>
     */
    public java.util.List<Integer>
        getCardsList() {
      return cards_;
    }
    /**
     * <code>repeated int32 cards = 2;</code>
     */
    public int getCardsCount() {
      return cards_.size();
    }
    /**
     * <code>repeated int32 cards = 2;</code>
     */
    public int getCards(int index) {
      return cards_.get(index);
    }
    private int cardsMemoizedSerializedSize = -1;

    public static final int DZCARDS_FIELD_NUMBER = 3;
    private java.util.List<Integer> dzCards_;
    /**
     * <code>repeated int32 dzCards = 3;</code>
     */
    public java.util.List<Integer>
        getDzCardsList() {
      return dzCards_;
    }
    /**
     * <code>repeated int32 dzCards = 3;</code>
     */
    public int getDzCardsCount() {
      return dzCards_.size();
    }
    /**
     * <code>repeated int32 dzCards = 3;</code>
     */
    public int getDzCards(int index) {
      return dzCards_.get(index);
    }
    private int dzCardsMemoizedSerializedSize = -1;

    public static final int ISCHOOSEDZ_FIELD_NUMBER = 4;
    private boolean isChooseDz_;
    /**
     * <code>optional bool isChooseDz = 4;</code>
     */
    public boolean getIsChooseDz() {
      return isChooseDz_;
    }

    public static final int PREVIOUSUSERNAME_FIELD_NUMBER = 5;
    private volatile Object previousUsername_;
    /**
     * <code>optional string previousUsername = 5;</code>
     */
    public String getPreviousUsername() {
      Object ref = previousUsername_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        previousUsername_ = s;
        return s;
      }
    }
    /**
     * <code>optional string previousUsername = 5;</code>
     */
    public com.google.protobuf.ByteString
        getPreviousUsernameBytes() {
      Object ref = previousUsername_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        previousUsername_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int NEXTUSERNAME_FIELD_NUMBER = 6;
    private volatile Object nextUsername_;
    /**
     * <code>optional string nextUsername = 6;</code>
     */
    public String getNextUsername() {
      Object ref = nextUsername_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        nextUsername_ = s;
        return s;
      }
    }
    /**
     * <code>optional string nextUsername = 6;</code>
     */
    public com.google.protobuf.ByteString
        getNextUsernameBytes() {
      Object ref = nextUsername_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        nextUsername_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (!getTiBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, ti_);
      }
      if (getCardsList().size() > 0) {
        output.writeUInt32NoTag(18);
        output.writeUInt32NoTag(cardsMemoizedSerializedSize);
      }
      for (int i = 0; i < cards_.size(); i++) {
        output.writeInt32NoTag(cards_.get(i));
      }
      if (getDzCardsList().size() > 0) {
        output.writeUInt32NoTag(26);
        output.writeUInt32NoTag(dzCardsMemoizedSerializedSize);
      }
      for (int i = 0; i < dzCards_.size(); i++) {
        output.writeInt32NoTag(dzCards_.get(i));
      }
      if (isChooseDz_ != false) {
        output.writeBool(4, isChooseDz_);
      }
      if (!getPreviousUsernameBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 5, previousUsername_);
      }
      if (!getNextUsernameBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 6, nextUsername_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getTiBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, ti_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < cards_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(cards_.get(i));
        }
        size += dataSize;
        if (!getCardsList().isEmpty()) {
          size += 1;
          size += com.google.protobuf.CodedOutputStream
              .computeInt32SizeNoTag(dataSize);
        }
        cardsMemoizedSerializedSize = dataSize;
      }
      {
        int dataSize = 0;
        for (int i = 0; i < dzCards_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(dzCards_.get(i));
        }
        size += dataSize;
        if (!getDzCardsList().isEmpty()) {
          size += 1;
          size += com.google.protobuf.CodedOutputStream
              .computeInt32SizeNoTag(dataSize);
        }
        dzCardsMemoizedSerializedSize = dataSize;
      }
      if (isChooseDz_ != false) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(4, isChooseDz_);
      }
      if (!getPreviousUsernameBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, previousUsername_);
      }
      if (!getNextUsernameBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(6, nextUsername_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof GameStartResponse)) {
        return super.equals(obj);
      }
      GameStartResponse other = (GameStartResponse) obj;

      boolean result = true;
      result = result && getTi()
          .equals(other.getTi());
      result = result && getCardsList()
          .equals(other.getCardsList());
      result = result && getDzCardsList()
          .equals(other.getDzCardsList());
      result = result && (getIsChooseDz()
          == other.getIsChooseDz());
      result = result && getPreviousUsername()
          .equals(other.getPreviousUsername());
      result = result && getNextUsername()
          .equals(other.getNextUsername());
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      hash = (37 * hash) + TI_FIELD_NUMBER;
      hash = (53 * hash) + getTi().hashCode();
      if (getCardsCount() > 0) {
        hash = (37 * hash) + CARDS_FIELD_NUMBER;
        hash = (53 * hash) + getCardsList().hashCode();
      }
      if (getDzCardsCount() > 0) {
        hash = (37 * hash) + DZCARDS_FIELD_NUMBER;
        hash = (53 * hash) + getDzCardsList().hashCode();
      }
      hash = (37 * hash) + ISCHOOSEDZ_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
          getIsChooseDz());
      hash = (37 * hash) + PREVIOUSUSERNAME_FIELD_NUMBER;
      hash = (53 * hash) + getPreviousUsername().hashCode();
      hash = (37 * hash) + NEXTUSERNAME_FIELD_NUMBER;
      hash = (53 * hash) + getNextUsername().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static GameStartResponse parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static GameStartResponse parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static GameStartResponse parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static GameStartResponse parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static GameStartResponse parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static GameStartResponse parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static GameStartResponse parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static GameStartResponse parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static GameStartResponse parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static GameStartResponse parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(GameStartResponse prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code GameStartResponse}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:GameStartResponse)
        GameStartResponseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return SocketResponse.internal_static_GameStartResponse_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return SocketResponse.internal_static_GameStartResponse_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                GameStartResponse.class, Builder.class);
      }

      // Construct using com.lawsofnature.gamecenter.common.domain.SocketResponse.GameStartResponse.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        ti_ = "";

        cards_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        dzCards_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000004);
        isChooseDz_ = false;

        previousUsername_ = "";

        nextUsername_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return SocketResponse.internal_static_GameStartResponse_descriptor;
      }

      public GameStartResponse getDefaultInstanceForType() {
        return GameStartResponse.getDefaultInstance();
      }

      public GameStartResponse build() {
        GameStartResponse result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public GameStartResponse buildPartial() {
        GameStartResponse result = new GameStartResponse(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        result.ti_ = ti_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          cards_ = java.util.Collections.unmodifiableList(cards_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.cards_ = cards_;
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          dzCards_ = java.util.Collections.unmodifiableList(dzCards_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.dzCards_ = dzCards_;
        result.isChooseDz_ = isChooseDz_;
        result.previousUsername_ = previousUsername_;
        result.nextUsername_ = nextUsername_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof GameStartResponse) {
          return mergeFrom((GameStartResponse)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(GameStartResponse other) {
        if (other == GameStartResponse.getDefaultInstance()) return this;
        if (!other.getTi().isEmpty()) {
          ti_ = other.ti_;
          onChanged();
        }
        if (!other.cards_.isEmpty()) {
          if (cards_.isEmpty()) {
            cards_ = other.cards_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureCardsIsMutable();
            cards_.addAll(other.cards_);
          }
          onChanged();
        }
        if (!other.dzCards_.isEmpty()) {
          if (dzCards_.isEmpty()) {
            dzCards_ = other.dzCards_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensureDzCardsIsMutable();
            dzCards_.addAll(other.dzCards_);
          }
          onChanged();
        }
        if (other.getIsChooseDz() != false) {
          setIsChooseDz(other.getIsChooseDz());
        }
        if (!other.getPreviousUsername().isEmpty()) {
          previousUsername_ = other.previousUsername_;
          onChanged();
        }
        if (!other.getNextUsername().isEmpty()) {
          nextUsername_ = other.nextUsername_;
          onChanged();
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        GameStartResponse parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (GameStartResponse) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private Object ti_ = "";
      /**
       * <code>optional string ti = 1;</code>
       */
      public String getTi() {
        Object ref = ti_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          ti_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string ti = 1;</code>
       */
      public com.google.protobuf.ByteString
          getTiBytes() {
        Object ref = ti_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          ti_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string ti = 1;</code>
       */
      public Builder setTi(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        ti_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string ti = 1;</code>
       */
      public Builder clearTi() {
        
        ti_ = getDefaultInstance().getTi();
        onChanged();
        return this;
      }
      /**
       * <code>optional string ti = 1;</code>
       */
      public Builder setTiBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        ti_ = value;
        onChanged();
        return this;
      }

      private java.util.List<Integer> cards_ = java.util.Collections.emptyList();
      private void ensureCardsIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          cards_ = new java.util.ArrayList<Integer>(cards_);
          bitField0_ |= 0x00000002;
         }
      }
      /**
       * <code>repeated int32 cards = 2;</code>
       */
      public java.util.List<Integer>
          getCardsList() {
        return java.util.Collections.unmodifiableList(cards_);
      }
      /**
       * <code>repeated int32 cards = 2;</code>
       */
      public int getCardsCount() {
        return cards_.size();
      }
      /**
       * <code>repeated int32 cards = 2;</code>
       */
      public int getCards(int index) {
        return cards_.get(index);
      }
      /**
       * <code>repeated int32 cards = 2;</code>
       */
      public Builder setCards(
          int index, int value) {
        ensureCardsIsMutable();
        cards_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 cards = 2;</code>
       */
      public Builder addCards(int value) {
        ensureCardsIsMutable();
        cards_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 cards = 2;</code>
       */
      public Builder addAllCards(
          Iterable<? extends Integer> values) {
        ensureCardsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, cards_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 cards = 2;</code>
       */
      public Builder clearCards() {
        cards_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }

      private java.util.List<Integer> dzCards_ = java.util.Collections.emptyList();
      private void ensureDzCardsIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          dzCards_ = new java.util.ArrayList<Integer>(dzCards_);
          bitField0_ |= 0x00000004;
         }
      }
      /**
       * <code>repeated int32 dzCards = 3;</code>
       */
      public java.util.List<Integer>
          getDzCardsList() {
        return java.util.Collections.unmodifiableList(dzCards_);
      }
      /**
       * <code>repeated int32 dzCards = 3;</code>
       */
      public int getDzCardsCount() {
        return dzCards_.size();
      }
      /**
       * <code>repeated int32 dzCards = 3;</code>
       */
      public int getDzCards(int index) {
        return dzCards_.get(index);
      }
      /**
       * <code>repeated int32 dzCards = 3;</code>
       */
      public Builder setDzCards(
          int index, int value) {
        ensureDzCardsIsMutable();
        dzCards_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 dzCards = 3;</code>
       */
      public Builder addDzCards(int value) {
        ensureDzCardsIsMutable();
        dzCards_.add(value);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 dzCards = 3;</code>
       */
      public Builder addAllDzCards(
          Iterable<? extends Integer> values) {
        ensureDzCardsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, dzCards_);
        onChanged();
        return this;
      }
      /**
       * <code>repeated int32 dzCards = 3;</code>
       */
      public Builder clearDzCards() {
        dzCards_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000004);
        onChanged();
        return this;
      }

      private boolean isChooseDz_ ;
      /**
       * <code>optional bool isChooseDz = 4;</code>
       */
      public boolean getIsChooseDz() {
        return isChooseDz_;
      }
      /**
       * <code>optional bool isChooseDz = 4;</code>
       */
      public Builder setIsChooseDz(boolean value) {
        
        isChooseDz_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bool isChooseDz = 4;</code>
       */
      public Builder clearIsChooseDz() {
        
        isChooseDz_ = false;
        onChanged();
        return this;
      }

      private Object previousUsername_ = "";
      /**
       * <code>optional string previousUsername = 5;</code>
       */
      public String getPreviousUsername() {
        Object ref = previousUsername_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          previousUsername_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string previousUsername = 5;</code>
       */
      public com.google.protobuf.ByteString
          getPreviousUsernameBytes() {
        Object ref = previousUsername_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          previousUsername_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string previousUsername = 5;</code>
       */
      public Builder setPreviousUsername(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        previousUsername_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string previousUsername = 5;</code>
       */
      public Builder clearPreviousUsername() {
        
        previousUsername_ = getDefaultInstance().getPreviousUsername();
        onChanged();
        return this;
      }
      /**
       * <code>optional string previousUsername = 5;</code>
       */
      public Builder setPreviousUsernameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        previousUsername_ = value;
        onChanged();
        return this;
      }

      private Object nextUsername_ = "";
      /**
       * <code>optional string nextUsername = 6;</code>
       */
      public String getNextUsername() {
        Object ref = nextUsername_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          nextUsername_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>optional string nextUsername = 6;</code>
       */
      public com.google.protobuf.ByteString
          getNextUsernameBytes() {
        Object ref = nextUsername_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          nextUsername_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string nextUsername = 6;</code>
       */
      public Builder setNextUsername(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        nextUsername_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string nextUsername = 6;</code>
       */
      public Builder clearNextUsername() {
        
        nextUsername_ = getDefaultInstance().getNextUsername();
        onChanged();
        return this;
      }
      /**
       * <code>optional string nextUsername = 6;</code>
       */
      public Builder setNextUsernameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        nextUsername_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:GameStartResponse)
    }

    // @@protoc_insertion_point(class_scope:GameStartResponse)
    private static final GameStartResponse DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new GameStartResponse();
    }

    public static GameStartResponse getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<GameStartResponse>
        PARSER = new com.google.protobuf.AbstractParser<GameStartResponse>() {
      public GameStartResponse parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new GameStartResponse(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<GameStartResponse> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<GameStartResponse> getParserForType() {
      return PARSER;
    }

    public GameStartResponse getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_GameStartResponse_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_GameStartResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\031src/socket_response.proto\"\203\001\n\021GameStar" +
      "tResponse\022\n\n\002ti\030\001 \001(\t\022\r\n\005cards\030\002 \003(\005\022\017\n\007" +
      "dzCards\030\003 \003(\005\022\022\n\nisChooseDz\030\004 \001(\010\022\030\n\020pre" +
      "viousUsername\030\005 \001(\t\022\024\n\014nextUsername\030\006 \001(" +
      "\tB;\n)com.lawsofnature.gamecenter.common." +
      "domainB\016SocketResponseb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_GameStartResponse_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_GameStartResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_GameStartResponse_descriptor,
        new String[] { "Ti", "Cards", "DzCards", "IsChooseDz", "PreviousUsername", "NextUsername", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
