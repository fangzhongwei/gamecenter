/**
 * Generated by Scrooge
 *   version: 4.5.0
 *   rev: 014664de600267b36809bbc85225e26aec286216
 *   built at: 20160203-205352
 */
package com.jxjxgo.game.rpc.domain

import com.twitter.scrooge.{
  LazyTProtocol,
  TFieldBlob, ThriftException, ThriftStruct, ThriftStructCodec3, ThriftStructFieldInfo,
  ThriftStructMetaData, ThriftUtil}
import org.apache.thrift.protocol._
import org.apache.thrift.transport.{TMemoryBuffer, TTransport}
import java.nio.ByteBuffer
import java.util.Arrays
import scala.collection.immutable.{Map => immutable$Map}
import scala.collection.mutable.Builder
import scala.collection.mutable.{
  ArrayBuffer => mutable$ArrayBuffer, Buffer => mutable$Buffer,
  HashMap => mutable$HashMap, HashSet => mutable$HashSet}
import scala.collection.{Map, Set}


object JoinGameResponse extends ThriftStructCodec3[JoinGameResponse] {
  private val NoPassthroughFields = immutable$Map.empty[Short, TFieldBlob]
  val Struct = new TStruct("JoinGameResponse")
  val CodeField = new TField("code", TType.STRING, 1)
  val CodeFieldManifest = implicitly[Manifest[String]]
  val MemberIdField = new TField("memberId", TType.I64, 2)
  val MemberIdFieldManifest = implicitly[Manifest[Long]]
  val TurnField = new TField("turn", TType.STRUCT, 3)
  val TurnFieldManifest = implicitly[Manifest[com.jxjxgo.game.rpc.domain.GameTurnResponse]]

  /**
   * Field information in declaration order.
   */
  lazy val fieldInfos: scala.List[ThriftStructFieldInfo] = scala.List[ThriftStructFieldInfo](
    new ThriftStructFieldInfo(
      CodeField,
      false,
      false,
      CodeFieldManifest,
      _root_.scala.None,
      _root_.scala.None,
      immutable$Map.empty[String, String],
      immutable$Map.empty[String, String]
    ),
    new ThriftStructFieldInfo(
      MemberIdField,
      false,
      false,
      MemberIdFieldManifest,
      _root_.scala.None,
      _root_.scala.None,
      immutable$Map.empty[String, String],
      immutable$Map.empty[String, String]
    ),
    new ThriftStructFieldInfo(
      TurnField,
      false,
      false,
      TurnFieldManifest,
      _root_.scala.None,
      _root_.scala.None,
      immutable$Map.empty[String, String],
      immutable$Map.empty[String, String]
    )
  )

  lazy val structAnnotations: immutable$Map[String, String] =
    immutable$Map.empty[String, String]

  /**
   * Checks that all required fields are non-null.
   */
  def validate(_item: JoinGameResponse): Unit = {
  }

  def withoutPassthroughFields(original: JoinGameResponse): JoinGameResponse =
    new Immutable(
      code =
        {
          val field = original.code
          field
        },
      memberId =
        {
          val field = original.memberId
          field
        },
      turn =
        {
          val field = original.turn
          com.jxjxgo.game.rpc.domain.GameTurnResponse.withoutPassthroughFields(field)
        }
    )

  override def encode(_item: JoinGameResponse, _oproto: TProtocol): Unit = {
    _item.write(_oproto)
  }

  private[this] def lazyDecode(_iprot: LazyTProtocol): JoinGameResponse = {

    var codeOffset: Int = -1
    var memberId: Long = 0L
    var turn: com.jxjxgo.game.rpc.domain.GameTurnResponse = null

    var _passthroughFields: Builder[(Short, TFieldBlob), immutable$Map[Short, TFieldBlob]] = null
    var _done = false
    val _start_offset = _iprot.offset

    _iprot.readStructBegin()
    while (!_done) {
      val _field = _iprot.readFieldBegin()
      if (_field.`type` == TType.STOP) {
        _done = true
      } else {
        _field.id match {
          case 1 =>
            _field.`type` match {
              case TType.STRING =>
                codeOffset = _iprot.offsetSkipString
    
              case _actualType =>
                val _expectedType = TType.STRING
                throw new TProtocolException(
                  "Received wrong type for field 'code' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case 2 =>
            _field.`type` match {
              case TType.I64 =>
    
                memberId = readMemberIdValue(_iprot)
              case _actualType =>
                val _expectedType = TType.I64
                throw new TProtocolException(
                  "Received wrong type for field 'memberId' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case 3 =>
            _field.`type` match {
              case TType.STRUCT =>
    
                turn = readTurnValue(_iprot)
              case _actualType =>
                val _expectedType = TType.STRUCT
                throw new TProtocolException(
                  "Received wrong type for field 'turn' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case _ =>
            if (_passthroughFields == null)
              _passthroughFields = immutable$Map.newBuilder[Short, TFieldBlob]
            _passthroughFields += (_field.id -> TFieldBlob.read(_field, _iprot))
        }
        _iprot.readFieldEnd()
      }
    }
    _iprot.readStructEnd()

    new LazyImmutable(
      _iprot,
      _iprot.buffer,
      _start_offset,
      _iprot.offset,
      codeOffset,
      memberId,
      turn,
      if (_passthroughFields == null)
        NoPassthroughFields
      else
        _passthroughFields.result()
    )
  }

  override def decode(_iprot: TProtocol): JoinGameResponse =
    _iprot match {
      case i: LazyTProtocol => lazyDecode(i)
      case i => eagerDecode(i)
    }

  private[this] def eagerDecode(_iprot: TProtocol): JoinGameResponse = {
    var code: String = ""
    var memberId: Long = 0L
    var turn: com.jxjxgo.game.rpc.domain.GameTurnResponse = null
    var _passthroughFields: Builder[(Short, TFieldBlob), immutable$Map[Short, TFieldBlob]] = null
    var _done = false

    _iprot.readStructBegin()
    while (!_done) {
      val _field = _iprot.readFieldBegin()
      if (_field.`type` == TType.STOP) {
        _done = true
      } else {
        _field.id match {
          case 1 =>
            _field.`type` match {
              case TType.STRING =>
                code = readCodeValue(_iprot)
              case _actualType =>
                val _expectedType = TType.STRING
                throw new TProtocolException(
                  "Received wrong type for field 'code' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case 2 =>
            _field.`type` match {
              case TType.I64 =>
                memberId = readMemberIdValue(_iprot)
              case _actualType =>
                val _expectedType = TType.I64
                throw new TProtocolException(
                  "Received wrong type for field 'memberId' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case 3 =>
            _field.`type` match {
              case TType.STRUCT =>
                turn = readTurnValue(_iprot)
              case _actualType =>
                val _expectedType = TType.STRUCT
                throw new TProtocolException(
                  "Received wrong type for field 'turn' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case _ =>
            if (_passthroughFields == null)
              _passthroughFields = immutable$Map.newBuilder[Short, TFieldBlob]
            _passthroughFields += (_field.id -> TFieldBlob.read(_field, _iprot))
        }
        _iprot.readFieldEnd()
      }
    }
    _iprot.readStructEnd()

    new Immutable(
      code,
      memberId,
      turn,
      if (_passthroughFields == null)
        NoPassthroughFields
      else
        _passthroughFields.result()
    )
  }

  def apply(
    code: String = "",
    memberId: Long = 0L,
    turn: com.jxjxgo.game.rpc.domain.GameTurnResponse
  ): JoinGameResponse =
    new Immutable(
      code,
      memberId,
      turn
    )

  def unapply(_item: JoinGameResponse): _root_.scala.Option[scala.Product3[String, Long, com.jxjxgo.game.rpc.domain.GameTurnResponse]] = _root_.scala.Some(_item)


  @inline private def readCodeValue(_iprot: TProtocol): String = {
    _iprot.readString()
  }

  @inline private def writeCodeField(code_item: String, _oprot: TProtocol): Unit = {
    _oprot.writeFieldBegin(CodeField)
    writeCodeValue(code_item, _oprot)
    _oprot.writeFieldEnd()
  }

  @inline private def writeCodeValue(code_item: String, _oprot: TProtocol): Unit = {
    _oprot.writeString(code_item)
  }

  @inline private def readMemberIdValue(_iprot: TProtocol): Long = {
    _iprot.readI64()
  }

  @inline private def writeMemberIdField(memberId_item: Long, _oprot: TProtocol): Unit = {
    _oprot.writeFieldBegin(MemberIdField)
    writeMemberIdValue(memberId_item, _oprot)
    _oprot.writeFieldEnd()
  }

  @inline private def writeMemberIdValue(memberId_item: Long, _oprot: TProtocol): Unit = {
    _oprot.writeI64(memberId_item)
  }

  @inline private def readTurnValue(_iprot: TProtocol): com.jxjxgo.game.rpc.domain.GameTurnResponse = {
    com.jxjxgo.game.rpc.domain.GameTurnResponse.decode(_iprot)
  }

  @inline private def writeTurnField(turn_item: com.jxjxgo.game.rpc.domain.GameTurnResponse, _oprot: TProtocol): Unit = {
    _oprot.writeFieldBegin(TurnField)
    writeTurnValue(turn_item, _oprot)
    _oprot.writeFieldEnd()
  }

  @inline private def writeTurnValue(turn_item: com.jxjxgo.game.rpc.domain.GameTurnResponse, _oprot: TProtocol): Unit = {
    turn_item.write(_oprot)
  }


  object Immutable extends ThriftStructCodec3[JoinGameResponse] {
    override def encode(_item: JoinGameResponse, _oproto: TProtocol): Unit = { _item.write(_oproto) }
    override def decode(_iprot: TProtocol): JoinGameResponse = JoinGameResponse.decode(_iprot)
    override lazy val metaData: ThriftStructMetaData[JoinGameResponse] = JoinGameResponse.metaData
  }

  /**
   * The default read-only implementation of JoinGameResponse.  You typically should not need to
   * directly reference this class; instead, use the JoinGameResponse.apply method to construct
   * new instances.
   */
  class Immutable(
      val code: String,
      val memberId: Long,
      val turn: com.jxjxgo.game.rpc.domain.GameTurnResponse,
      override val _passthroughFields: immutable$Map[Short, TFieldBlob])
    extends JoinGameResponse {
    def this(
      code: String = "",
      memberId: Long = 0L,
      turn: com.jxjxgo.game.rpc.domain.GameTurnResponse
    ) = this(
      code,
      memberId,
      turn,
      Map.empty
    )
  }

  /**
   * This is another Immutable, this however keeps strings as lazy values that are lazily decoded from the backing
   * array byte on read.
   */
  private[this] class LazyImmutable(
      _proto: LazyTProtocol,
      _buf: Array[Byte],
      _start_offset: Int,
      _end_offset: Int,
      codeOffset: Int,
      val memberId: Long,
      val turn: com.jxjxgo.game.rpc.domain.GameTurnResponse,
      override val _passthroughFields: immutable$Map[Short, TFieldBlob])
    extends JoinGameResponse {

    override def write(_oprot: TProtocol): Unit = {
      _oprot match {
        case i: LazyTProtocol => i.writeRaw(_buf, _start_offset, _end_offset - _start_offset)
        case _ => super.write(_oprot)
      }
    }

    lazy val code: String =
      if (codeOffset == -1)
        ""
      else {
        _proto.decodeString(_buf, codeOffset)
      }

    /**
     * Override the super hash code to make it a lazy val rather than def.
     *
     * Calculating the hash code can be expensive, caching it where possible
     * can provide significant performance wins. (Key in a hash map for instance)
     * Usually not safe since the normal constructor will accept a mutable map or
     * set as an arg
     * Here however we control how the class is generated from serialized data.
     * With the class private and the contract that we throw away our mutable references
     * having the hash code lazy here is safe.
     */
    override lazy val hashCode = super.hashCode
  }

  /**
   * This Proxy trait allows you to extend the JoinGameResponse trait with additional state or
   * behavior and implement the read-only methods from JoinGameResponse using an underlying
   * instance.
   */
  trait Proxy extends JoinGameResponse {
    protected def _underlying_JoinGameResponse: JoinGameResponse
    override def code: String = _underlying_JoinGameResponse.code
    override def memberId: Long = _underlying_JoinGameResponse.memberId
    override def turn: com.jxjxgo.game.rpc.domain.GameTurnResponse = _underlying_JoinGameResponse.turn
    override def _passthroughFields = _underlying_JoinGameResponse._passthroughFields
  }
}

trait JoinGameResponse
  extends ThriftStruct
  with scala.Product3[String, Long, com.jxjxgo.game.rpc.domain.GameTurnResponse]
  with java.io.Serializable
{
  import JoinGameResponse._

  def code: String
  def memberId: Long
  def turn: com.jxjxgo.game.rpc.domain.GameTurnResponse

  def _passthroughFields: immutable$Map[Short, TFieldBlob] = immutable$Map.empty

  def _1 = code
  def _2 = memberId
  def _3 = turn


  /**
   * Gets a field value encoded as a binary blob using TCompactProtocol.  If the specified field
   * is present in the passthrough map, that value is returned.  Otherwise, if the specified field
   * is known and not optional and set to None, then the field is serialized and returned.
   */
  def getFieldBlob(_fieldId: Short): _root_.scala.Option[TFieldBlob] = {
    lazy val _buff = new TMemoryBuffer(32)
    lazy val _oprot = new TCompactProtocol(_buff)
    _passthroughFields.get(_fieldId) match {
      case blob: _root_.scala.Some[TFieldBlob] => blob
      case _root_.scala.None => {
        val _fieldOpt: _root_.scala.Option[TField] =
          _fieldId match {
            case 1 =>
              if (code ne null) {
                writeCodeValue(code, _oprot)
                _root_.scala.Some(JoinGameResponse.CodeField)
              } else {
                _root_.scala.None
              }
            case 2 =>
              if (true) {
                writeMemberIdValue(memberId, _oprot)
                _root_.scala.Some(JoinGameResponse.MemberIdField)
              } else {
                _root_.scala.None
              }
            case 3 =>
              if (turn ne null) {
                writeTurnValue(turn, _oprot)
                _root_.scala.Some(JoinGameResponse.TurnField)
              } else {
                _root_.scala.None
              }
            case _ => _root_.scala.None
          }
        _fieldOpt match {
          case _root_.scala.Some(_field) =>
            val _data = Arrays.copyOfRange(_buff.getArray, 0, _buff.length)
            _root_.scala.Some(TFieldBlob(_field, _data))
          case _root_.scala.None =>
            _root_.scala.None
        }
      }
    }
  }

  /**
   * Collects TCompactProtocol-encoded field values according to `getFieldBlob` into a map.
   */
  def getFieldBlobs(ids: TraversableOnce[Short]): immutable$Map[Short, TFieldBlob] =
    (ids flatMap { id => getFieldBlob(id) map { id -> _ } }).toMap

  /**
   * Sets a field using a TCompactProtocol-encoded binary blob.  If the field is a known
   * field, the blob is decoded and the field is set to the decoded value.  If the field
   * is unknown and passthrough fields are enabled, then the blob will be stored in
   * _passthroughFields.
   */
  def setField(_blob: TFieldBlob): JoinGameResponse = {
    var code: String = this.code
    var memberId: Long = this.memberId
    var turn: com.jxjxgo.game.rpc.domain.GameTurnResponse = this.turn
    var _passthroughFields = this._passthroughFields
    _blob.id match {
      case 1 =>
        code = readCodeValue(_blob.read)
      case 2 =>
        memberId = readMemberIdValue(_blob.read)
      case 3 =>
        turn = readTurnValue(_blob.read)
      case _ => _passthroughFields += (_blob.id -> _blob)
    }
    new Immutable(
      code,
      memberId,
      turn,
      _passthroughFields
    )
  }

  /**
   * If the specified field is optional, it is set to None.  Otherwise, if the field is
   * known, it is reverted to its default value; if the field is unknown, it is removed
   * from the passthroughFields map, if present.
   */
  def unsetField(_fieldId: Short): JoinGameResponse = {
    var code: String = this.code
    var memberId: Long = this.memberId
    var turn: com.jxjxgo.game.rpc.domain.GameTurnResponse = this.turn

    _fieldId match {
      case 1 =>
        code = ""
      case 2 =>
        memberId = 0L
      case 3 =>
        turn = null
      case _ =>
    }
    new Immutable(
      code,
      memberId,
      turn,
      _passthroughFields - _fieldId
    )
  }

  /**
   * If the specified field is optional, it is set to None.  Otherwise, if the field is
   * known, it is reverted to its default value; if the field is unknown, it is removed
   * from the passthroughFields map, if present.
   */
  def unsetCode: JoinGameResponse = unsetField(1)

  def unsetMemberId: JoinGameResponse = unsetField(2)

  def unsetTurn: JoinGameResponse = unsetField(3)


  override def write(_oprot: TProtocol): Unit = {
    JoinGameResponse.validate(this)
    _oprot.writeStructBegin(Struct)
    if (code ne null) writeCodeField(code, _oprot)
    writeMemberIdField(memberId, _oprot)
    if (turn ne null) writeTurnField(turn, _oprot)
    if (_passthroughFields.nonEmpty) {
      _passthroughFields.values.foreach { _.write(_oprot) }
    }
    _oprot.writeFieldStop()
    _oprot.writeStructEnd()
  }

  def copy(
    code: String = this.code,
    memberId: Long = this.memberId,
    turn: com.jxjxgo.game.rpc.domain.GameTurnResponse = this.turn,
    _passthroughFields: immutable$Map[Short, TFieldBlob] = this._passthroughFields
  ): JoinGameResponse =
    new Immutable(
      code,
      memberId,
      turn,
      _passthroughFields
    )

  override def canEqual(other: Any): Boolean = other.isInstanceOf[JoinGameResponse]

  override def equals(other: Any): Boolean =
    canEqual(other) &&
      _root_.scala.runtime.ScalaRunTime._equals(this, other) &&
      _passthroughFields == other.asInstanceOf[JoinGameResponse]._passthroughFields

  override def hashCode: Int = _root_.scala.runtime.ScalaRunTime._hashCode(this)

  override def toString: String = _root_.scala.runtime.ScalaRunTime._toString(this)


  override def productArity: Int = 3

  override def productElement(n: Int): Any = n match {
    case 0 => this.code
    case 1 => this.memberId
    case 2 => this.turn
    case _ => throw new IndexOutOfBoundsException(n.toString)
  }

  override def productPrefix: String = "JoinGameResponse"
}