/**
 * Generated by Scrooge
 *   version: 4.5.0
 *   rev: 014664de600267b36809bbc85225e26aec286216
 *   built at: 20160203-205352
 */
package com.jxjxgo.gamecenter.rpc.domain

import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.{NullStatsReceiver, StatsReceiver}
import com.twitter.scrooge.{ThriftStruct, TReusableMemoryTransport}
import com.twitter.util.{Future, Return, Throw, Throwables}
import java.nio.ByteBuffer
import java.util.Arrays
import org.apache.thrift.protocol._
import org.apache.thrift.TApplicationException
import org.apache.thrift.transport.TMemoryInputTransport
import scala.collection.mutable.{
  ArrayBuffer => mutable$ArrayBuffer, HashMap => mutable$HashMap}
import scala.collection.{Map, Set}

import scala.language.higherKinds


@javax.annotation.Generated(value = Array("com.twitter.scrooge.Compiler"))
class GameEndpoint$FinagleService(
  iface: GameEndpoint[Future],
  protocolFactory: TProtocolFactory,
  stats: StatsReceiver,
  maxThriftBufferSize: Int,
  serviceName: String
) extends com.twitter.finagle.Service[Array[Byte], Array[Byte]] {
  import GameEndpoint._

  def this(
    iface: GameEndpoint[Future],
    protocolFactory: TProtocolFactory,
    stats: StatsReceiver,
    maxThriftBufferSize: Int
  ) = this(iface, protocolFactory, stats, maxThriftBufferSize, "GameEndpoint")

  def this(
    iface: GameEndpoint[Future],
    protocolFactory: TProtocolFactory
  ) = this(iface, protocolFactory, NullStatsReceiver, Thrift.maxThriftBufferSize)

  private[this] val tlReusableBuffer = new ThreadLocal[TReusableMemoryTransport] {
    override def initialValue() = TReusableMemoryTransport(512)
  }

  private[this] def reusableBuffer: TReusableMemoryTransport = {
    val buf = tlReusableBuffer.get()
    buf.reset()
    buf
  }

  private[this] val resetCounter = stats.scope("buffer").counter("resetCount")

  private[this] def resetBuffer(trans: TReusableMemoryTransport): Unit = {
    if (trans.currentCapacity > maxThriftBufferSize) {
      resetCounter.incr()
      tlReusableBuffer.remove()
    }
  }

  protected val functionMap = new mutable$HashMap[String, (TProtocol, Int) => Future[Array[Byte]]]()

  protected def addFunction(name: String, f: (TProtocol, Int) => Future[Array[Byte]]) {
    functionMap(name) = f
  }

  protected def exception(name: String, seqid: Int, code: Int, message: String): Future[Array[Byte]] = {
    try {
      val x = new TApplicationException(code, message)
      val memoryBuffer = reusableBuffer
      try {
        val oprot = protocolFactory.getProtocol(memoryBuffer)

        oprot.writeMessageBegin(new TMessage(name, TMessageType.EXCEPTION, seqid))
        x.write(oprot)
        oprot.writeMessageEnd()
        oprot.getTransport().flush()
        Future.value(Arrays.copyOfRange(memoryBuffer.getArray(), 0, memoryBuffer.length()))
      } finally {
        resetBuffer(memoryBuffer)
      }
    } catch {
      case e: Exception => Future.exception(e)
    }
  }

  protected def reply(name: String, seqid: Int, result: ThriftStruct): Future[Array[Byte]] = {
    try {
      val memoryBuffer = reusableBuffer
      try {
        val oprot = protocolFactory.getProtocol(memoryBuffer)

        oprot.writeMessageBegin(new TMessage(name, TMessageType.REPLY, seqid))
        result.write(oprot)
        oprot.writeMessageEnd()

        Future.value(Arrays.copyOfRange(memoryBuffer.getArray(), 0, memoryBuffer.length()))
      } finally {
        resetBuffer(memoryBuffer)
      }
    } catch {
      case e: Exception => Future.exception(e)
    }
  }

  final def apply(request: Array[Byte]): Future[Array[Byte]] = {
    val inputTransport = new TMemoryInputTransport(request)
    val iprot = protocolFactory.getProtocol(inputTransport)

    try {
      val msg = iprot.readMessageBegin()
      val func = functionMap.get(msg.name)
      func match {
        case _root_.scala.Some(fn) =>
          fn(iprot, msg.seqid)
        case _ =>
          TProtocolUtil.skip(iprot, TType.STRUCT)
          exception(msg.name, msg.seqid, TApplicationException.UNKNOWN_METHOD,
            "Invalid method name: '" + msg.name + "'")
      }
    } catch {
      case e: Exception => Future.exception(e)
    }
  }

  // ---- end boilerplate.

  private[this] val scopedStats = if (serviceName != "") stats.scope(serviceName) else stats
  private[this] object __stats_playerOnline {
    val RequestsCounter = scopedStats.scope("playerOnline").counter("requests")
    val SuccessCounter = scopedStats.scope("playerOnline").counter("success")
    val FailuresCounter = scopedStats.scope("playerOnline").counter("failures")
    val FailuresScope = scopedStats.scope("playerOnline").scope("failures")
  }
  addFunction("playerOnline", { (iprot: TProtocol, seqid: Int) =>
    try {
      __stats_playerOnline.RequestsCounter.incr()
      val args = PlayerOnline.Args.decode(iprot)
      iprot.readMessageEnd()
      (try {
        iface.playerOnline(args.traceId, args.request)
      } catch {
        case e: Exception => Future.exception(e)
      }).flatMap { value: com.jxjxgo.gamecenter.rpc.domain.GameBaseResponse =>
        reply("playerOnline", seqid, PlayerOnline.Result(success = Some(value)))
      }.rescue {
        case e => Future.exception(e)
      }.respond {
        case Return(_) =>
          __stats_playerOnline.SuccessCounter.incr()
        case Throw(ex) =>
          __stats_playerOnline.FailuresCounter.incr()
          __stats_playerOnline.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
      }
    } catch {
      case e: TProtocolException => {
        iprot.readMessageEnd()
        exception("playerOnline", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
      }
      case e: Exception => Future.exception(e)
    }
  })
  private[this] object __stats_playerOffline {
    val RequestsCounter = scopedStats.scope("playerOffline").counter("requests")
    val SuccessCounter = scopedStats.scope("playerOffline").counter("success")
    val FailuresCounter = scopedStats.scope("playerOffline").counter("failures")
    val FailuresScope = scopedStats.scope("playerOffline").scope("failures")
  }
  addFunction("playerOffline", { (iprot: TProtocol, seqid: Int) =>
    try {
      __stats_playerOffline.RequestsCounter.incr()
      val args = PlayerOffline.Args.decode(iprot)
      iprot.readMessageEnd()
      (try {
        iface.playerOffline(args.traceId, args.socketUuid, args.memberId)
      } catch {
        case e: Exception => Future.exception(e)
      }).flatMap { value: com.jxjxgo.gamecenter.rpc.domain.GameBaseResponse =>
        reply("playerOffline", seqid, PlayerOffline.Result(success = Some(value)))
      }.rescue {
        case e => Future.exception(e)
      }.respond {
        case Return(_) =>
          __stats_playerOffline.SuccessCounter.incr()
        case Throw(ex) =>
          __stats_playerOffline.FailuresCounter.incr()
          __stats_playerOffline.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
      }
    } catch {
      case e: TProtocolException => {
        iprot.readMessageEnd()
        exception("playerOffline", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
      }
      case e: Exception => Future.exception(e)
    }
  })
  private[this] object __stats_generateSocketId {
    val RequestsCounter = scopedStats.scope("generateSocketId").counter("requests")
    val SuccessCounter = scopedStats.scope("generateSocketId").counter("success")
    val FailuresCounter = scopedStats.scope("generateSocketId").counter("failures")
    val FailuresScope = scopedStats.scope("generateSocketId").scope("failures")
  }
  addFunction("generateSocketId", { (iprot: TProtocol, seqid: Int) =>
    try {
      __stats_generateSocketId.RequestsCounter.incr()
      val args = GenerateSocketId.Args.decode(iprot)
      iprot.readMessageEnd()
      (try {
        iface.generateSocketId(args.traceId)
      } catch {
        case e: Exception => Future.exception(e)
      }).flatMap { value: com.jxjxgo.gamecenter.rpc.domain.GenerateSocketIdResponse =>
        reply("generateSocketId", seqid, GenerateSocketId.Result(success = Some(value)))
      }.rescue {
        case e => Future.exception(e)
      }.respond {
        case Return(_) =>
          __stats_generateSocketId.SuccessCounter.incr()
        case Throw(ex) =>
          __stats_generateSocketId.FailuresCounter.incr()
          __stats_generateSocketId.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
      }
    } catch {
      case e: TProtocolException => {
        iprot.readMessageEnd()
        exception("generateSocketId", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
      }
      case e: Exception => Future.exception(e)
    }
  })
  private[this] object __stats_checkGameStatus {
    val RequestsCounter = scopedStats.scope("checkGameStatus").counter("requests")
    val SuccessCounter = scopedStats.scope("checkGameStatus").counter("success")
    val FailuresCounter = scopedStats.scope("checkGameStatus").counter("failures")
    val FailuresScope = scopedStats.scope("checkGameStatus").scope("failures")
  }
  addFunction("checkGameStatus", { (iprot: TProtocol, seqid: Int) =>
    try {
      __stats_checkGameStatus.RequestsCounter.incr()
      val args = CheckGameStatus.Args.decode(iprot)
      iprot.readMessageEnd()
      (try {
        iface.checkGameStatus(args.traceId, args.request)
      } catch {
        case e: Exception => Future.exception(e)
      }).flatMap { value: com.jxjxgo.gamecenter.rpc.domain.CheckGameStatusResponse =>
        reply("checkGameStatus", seqid, CheckGameStatus.Result(success = Some(value)))
      }.rescue {
        case e => Future.exception(e)
      }.respond {
        case Return(_) =>
          __stats_checkGameStatus.SuccessCounter.incr()
        case Throw(ex) =>
          __stats_checkGameStatus.FailuresCounter.incr()
          __stats_checkGameStatus.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
      }
    } catch {
      case e: TProtocolException => {
        iprot.readMessageEnd()
        exception("checkGameStatus", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
      }
      case e: Exception => Future.exception(e)
    }
  })
  private[this] object __stats_joinGame {
    val RequestsCounter = scopedStats.scope("joinGame").counter("requests")
    val SuccessCounter = scopedStats.scope("joinGame").counter("success")
    val FailuresCounter = scopedStats.scope("joinGame").counter("failures")
    val FailuresScope = scopedStats.scope("joinGame").scope("failures")
  }
  addFunction("joinGame", { (iprot: TProtocol, seqid: Int) =>
    try {
      __stats_joinGame.RequestsCounter.incr()
      val args = JoinGame.Args.decode(iprot)
      iprot.readMessageEnd()
      (try {
        iface.joinGame(args.traceId, args.request)
      } catch {
        case e: Exception => Future.exception(e)
      }).flatMap { value: com.jxjxgo.gamecenter.rpc.domain.GameBaseResponse =>
        reply("joinGame", seqid, JoinGame.Result(success = Some(value)))
      }.rescue {
        case e => Future.exception(e)
      }.respond {
        case Return(_) =>
          __stats_joinGame.SuccessCounter.incr()
        case Throw(ex) =>
          __stats_joinGame.FailuresCounter.incr()
          __stats_joinGame.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
      }
    } catch {
      case e: TProtocolException => {
        iprot.readMessageEnd()
        exception("joinGame", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
      }
      case e: Exception => Future.exception(e)
    }
  })
  private[this] object __stats_takeLandlord {
    val RequestsCounter = scopedStats.scope("takeLandlord").counter("requests")
    val SuccessCounter = scopedStats.scope("takeLandlord").counter("success")
    val FailuresCounter = scopedStats.scope("takeLandlord").counter("failures")
    val FailuresScope = scopedStats.scope("takeLandlord").scope("failures")
  }
  addFunction("takeLandlord", { (iprot: TProtocol, seqid: Int) =>
    try {
      __stats_takeLandlord.RequestsCounter.incr()
      val args = TakeLandlord.Args.decode(iprot)
      iprot.readMessageEnd()
      (try {
        iface.takeLandlord(args.traceId, args.request)
      } catch {
        case e: Exception => Future.exception(e)
      }).flatMap { value: com.jxjxgo.gamecenter.rpc.domain.GameBaseResponse =>
        reply("takeLandlord", seqid, TakeLandlord.Result(success = Some(value)))
      }.rescue {
        case e => Future.exception(e)
      }.respond {
        case Return(_) =>
          __stats_takeLandlord.SuccessCounter.incr()
        case Throw(ex) =>
          __stats_takeLandlord.FailuresCounter.incr()
          __stats_takeLandlord.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
      }
    } catch {
      case e: TProtocolException => {
        iprot.readMessageEnd()
        exception("takeLandlord", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
      }
      case e: Exception => Future.exception(e)
    }
  })
  private[this] object __stats_playCards {
    val RequestsCounter = scopedStats.scope("playCards").counter("requests")
    val SuccessCounter = scopedStats.scope("playCards").counter("success")
    val FailuresCounter = scopedStats.scope("playCards").counter("failures")
    val FailuresScope = scopedStats.scope("playCards").scope("failures")
  }
  addFunction("playCards", { (iprot: TProtocol, seqid: Int) =>
    try {
      __stats_playCards.RequestsCounter.incr()
      val args = PlayCards.Args.decode(iprot)
      iprot.readMessageEnd()
      (try {
        iface.playCards(args.traceId, args.request)
      } catch {
        case e: Exception => Future.exception(e)
      }).flatMap { value: com.jxjxgo.gamecenter.rpc.domain.GameBaseResponse =>
        reply("playCards", seqid, PlayCards.Result(success = Some(value)))
      }.rescue {
        case e => Future.exception(e)
      }.respond {
        case Return(_) =>
          __stats_playCards.SuccessCounter.incr()
        case Throw(ex) =>
          __stats_playCards.FailuresCounter.incr()
          __stats_playCards.FailuresScope.counter(Throwables.mkString(ex): _*).incr()
      }
    } catch {
      case e: TProtocolException => {
        iprot.readMessageEnd()
        exception("playCards", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
      }
      case e: Exception => Future.exception(e)
    }
  })
}