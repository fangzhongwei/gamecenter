package com.lawsofnature.gamecenter.server

import java.net.InetSocketAddress

import akka.actor._
import akka.io.{IO, Tcp}
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.helper.{ByteHelper, GZipHelper}
import com.lawsofnature.gamecenter.enumnate.{ReadStep, SocketActionType}
import com.proto.FirstProtobuf
import com.proto.FirstProtobuf.TestBuf

object TCPServerApp extends App {
  val system = ActorSystem()
  system.actorOf(Props[TCPServer])
}

class TCPServer extends Actor with ActorLogging {

  import Tcp._
  import context.system

  val TCPPort = 4200

  IO(Tcp) ! Bind(self, new InetSocketAddress(TCPPort))

  def receive = {
    case b@Bound(addr) =>
      log.info("Bound To Port '{}' on address '{}'", TCPPort, addr)

    case CommandFailed(_: Bind) =>
      log.error("Binding Command Failed. Exiting.")
      context stop self

    case c@Connected(remote, local) =>
      log.info("Client Connected. Remote: {} Local: {}", remote, local)
      val handler = context.actorOf(Props[TCPHandler])
      val connection = sender()
      connection ! Register(handler)
  }
}

class TCPHandler extends Actor with ActorLogging {

  import Tcp._

  var socketActionType: SocketActionType = SocketActionType.Login
  var dataLength = 0
  var bufferBytes: Array[Byte] = new Array[Byte](0)
  var currentReadStep: ReadStep = ReadStep.ActionType

  def handData(dataArray: Array[Byte]) = {
    println(s"before decrypt length is:${dataArray.length}")

    val decrypt: Array[Byte] = DESUtils.decrypt(dataArray, "ABCD1234")

    println(s"after decrypt length is:${decrypt.length}")
    val uncompress: Array[Byte] = GZipHelper.uncompress(decrypt)

    println(s"after uncompress length is:${uncompress.length}")

    val tb: FirstProtobuf.TestBuf = TestBuf.parseFrom(uncompress)
    System.out.println(tb.getID)
    System.out.println(tb.getUrl)
    sender() ! "hand data finish."
  }

  def receive = {
    case Received(data) =>
      bufferBytes = ByteHelper.combineTowBytes(bufferBytes, data.toArray)
      handle(currentReadStep)
    case PeerClosed =>
      log.info(s"actor close, $self.")
      context stop self
    case an: Any =>
      println(an)
      log.info(s"receive something $an")
  }

  def handle(readStep: ReadStep): Unit = {
    readStep match {
      case ReadStep.ActionType =>
        readActionType match {
          case Some(actionType) =>
            socketActionType = actionType
            log.info(s"socketActionType is $socketActionType")
            currentReadStep = ReadStep.get(readStep.getNextCode)
            bufferBytes.length > 0 match {
              case true =>
                handle(currentReadStep)
              case false =>
            }
          case None =>
        }
      case ReadStep.DataLength =>
        readDataLength match {
          case Some(len) =>
            dataLength = len
            log.info(s"dataLength is $dataLength")
            currentReadStep = ReadStep.get(readStep.getNextCode)
            bufferBytes.length > 0 match {
              case true =>
                handle(currentReadStep)
              case false =>
            }
          case None =>
        }
      case ReadStep.DataContent =>
        readDataContent(dataLength) match {
          case Some(array) =>
            log.info(s"DataContent length is ${array.length}")
            currentReadStep = ReadStep.get(readStep.getNextCode)
            try {
              handData(array)
            } catch {
              case ex: Exception => log.error(ex, ex.getMessage)
            }
            bufferBytes.length > 0 match {
              case true => handle(currentReadStep)
              case false =>
            }
          case None => log.info("continue wait for data content data.")
        }
      case _ =>
    }
  }

  def readActionType: Option[SocketActionType] = {
    readBytes(ReadStep.ActionType) match {
      case Some(array) =>
        Some(SocketActionType.get(ByteHelper.byteArrayToInt(array)))
      case None =>
        log.info("continue wait for action type data.")
        None
    }
  }

  def readDataLength: (Option[Int]) = {
    readBytes(ReadStep.DataLength) match {
      case Some(array) =>
        Some(ByteHelper.byteArrayToInt(array))
      case None =>
        log.info("continue wait for data length data.")
        None
    }
  }

  def readDataContent(length: Int): (Option[Array[Byte]]) = readBytes(ReadStep.DataContent, length)

  def readBytes(step: ReadStep, length: Int = 0): Option[Array[Byte]] = {
    val readHeaderLength: Int = {
      step.isFixedLength match {
        case true => step.getFixedLength
        case false => length
      }
    }
    val remainingLength: Int = bufferBytes.length - readHeaderLength
    remainingLength >= 0 match {
      case true =>
        val bytes: Array[Byte] = ByteHelper.readHeader(bufferBytes, readHeaderLength)
        bufferBytes = ByteHelper.readTail(bufferBytes, remainingLength)
        Some(bytes)
      case false => None
    }
  }
}
