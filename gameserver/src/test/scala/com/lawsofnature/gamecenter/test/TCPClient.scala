package com.lawsofnature.gamecenter.test

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.io.{IO, Tcp}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import akka.util.{ByteString, CompactByteString}
import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.helper.{ByteHelper, GZipHelper}
import com.proto.FirstProtobuf

import scala.concurrent.duration._
import scala.language.postfixOps

object TCPClientApp extends App {
  //  val host = args(0)
  //  val port = Integer.parseInt(args(1))
  //  val numClients = Integer.parseInt(args(2))
  val system = ActorSystem()
  system.actorOf(Props(classOf[TCPClientMaster], new InetSocketAddress("localhost", 4200), 5))
}

class TCPClientMaster(remoteAddr: InetSocketAddress, numClients: Int) extends Actor with ActorLogging {
  var router = {
    val routees = Vector.fill(numClients) {
      val r = context.actorOf(Props(classOf[TCPClient], remoteAddr, numClients))
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case anything =>
      log.info("Got a work request, we don't handle anything.")
  }
}

class TCPClient(remoteAddr: InetSocketAddress, numClients: Int) extends Actor with ActorLogging {

  import Tcp._
  import context.{dispatcher, system}

  IO(Tcp) ! Connect(remoteAddr)

  var sendLength = true

  def receive = {
    case Received(data) =>
      println(new String(data.toArray))
    case CommandFailed(_: Connect) =>
      log.warning("connect failed")
      context stop self
    case r@Reconnect =>
      log.info("Reconnect request made. Trying...")
      IO(Tcp) ! Connect(remoteAddr)
    case c@Connected(remote, local) =>
      log.info("Connected Remote: '{}' Local: '{}'", remote, local)
      val connection = sender()
      connection ! Register(self)
      context become {
        case data: ByteString =>
          connection ! Write(data)
        case Tick =>
          connection ! Write(ByteString("Mr. Watson--come here--I want to see you."))
        case CommandFailed(w: Write) =>
          // O/S buffer was full
          log.warning("write failed")
        case Received(data) =>
          log.info("Received data: '{}'", data)
        case "close" =>
          connection ! Close
        case _: ConnectionClosed =>
          // TODO - Reconnect here
          log.warning("Connection closed")
          // roll back our current behavior
          context.unbecome()


          // quiesce for a minute
          val reconnect =
          system.scheduler.scheduleOnce(1 minute, self, Reconnect)
      }

      val builder: FirstProtobuf.TestBuf.Builder = FirstProtobuf.TestBuf.newBuilder

      builder.setID(198273)
      builder.setUrl("https://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.comhttps://www.google.com")

      val info: FirstProtobuf.TestBuf = builder.build
      val buf: Array[Byte] = info.toByteArray

      println(s"before gzip, length is ${buf.length}")

      val compressedArray: Array[Byte] = GZipHelper.compress(buf)
      println(s"after gzip, length is ${compressedArray.length}")

      val encrypt: Array[Byte] = DESUtils.encrypt(compressedArray, "ABCD1234")

      println(s"after encrypt, length is ${encrypt.length}")

//      val length: Int = GZipHelper.compress(buf).length
//
//      println(s"direct gizp length is $length")

      // schedule occasional ticks that we'll send echo messages to the remote end
      val towBytes: Array[Byte] = ByteHelper.combineTowBytes(ByteHelper.intToByteArray(1),ByteHelper.intToByteArray(encrypt.length))

      val cancellable =
      system.scheduler.schedule(0 milliseconds,
        5 seconds,
        self,
        CompactByteString(ByteHelper.combineTowBytes(towBytes, encrypt)))
    //CompactByteString(buf)

    case an: Any =>
      println(an)
      log.info(s"receive something $an")
  }
}

case object Tick

case object Reconnect
