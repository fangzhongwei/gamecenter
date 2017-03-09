package com.jxjxgo.gamecenter.service

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator
import com.jxjxgo.common.helper.UUIDHelper
import com.jxjxgo.common.redis.RedisClientTemplate
import com.jxjxgo.gamecenter.domain.Seat
import com.jxjxgo.gamecenter.repo.TowVsOneRepository
import com.jxjxgo.gamegateway.rpc.domain.{GameGatewayEndpoint, SocketResponse}
import com.jxjxgo.memberber.rpc.domain.MemberEndpoint
import com.twitter.finagle.Thrift
import com.twitter.util.Future
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2016/12/20.
  */
trait GameNotifyService {
  def notifySeatInfo(seat: Seat)
}

class GameNotifyServiceImpl @Inject()(towVsOneRepository: TowVsOneRepository, redisClientTemplate: RedisClientTemplate, memberClientService: MemberEndpoint[Future]) extends GameNotifyService {
  private[this] val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private[this] val map: ConcurrentHashMap[String, GameGatewayEndpoint[Future]] = new ConcurrentHashMap[String, GameGatewayEndpoint[Future]]()

  implicit def long2String(lo: Long): String = lo.toString

  implicit def int2String(in: Int): String = in.toString

  implicit def short2String(sh: Short): String = sh.toString

  implicit def bool2String(bo: Boolean): String = bo.toString

  override def notifySeatInfo(s: Seat): Unit = {
    val record: towVsOneRepository.TmOnlineRecordRow = towVsOneRepository.getOnlineRecord(s.socketId)
    val gameGatewayEndpoint: GameGatewayEndpoint[Future] = thriftClient(record.rpcHost)

    //    1: string code,
    //    2: string action,
    //    3: i64 gameId = 0,
    //    4: i32 gameType = 0,
    //    5: i32 deviceType = 0,
    //    6: string cards = "",
    //    7: string landlordCards = "",
    //    8: string proCardsInfo = "",
    //    9: i32 baseAmount = 0,
    //    10: i32 multiples = 0,
    //    11: string previousNickname = "",
    //    12: i32 previousCardsCount = 0,
    //    13: string nextNickname = "",
    //    14: i32 nextCardsCount = 0,
    //    15: string playStatus = "",
    //    16: bool landlord = false,
    //    17: string fingerPrint = "",
    //    18: long memberId = 0,
    //    19: long seatId = 0,



    gameGatewayEndpoint.push(UUIDHelper.generate(), SocketResponse("0", "seatWatch", s.gameId, s.gameType, s.deviceType, s.cards, s.landlordCards, s.proCardsInfo, s.baseAmount, s.multiples, s.previousNickname, s.previousCardsCount, s.nextNickname, s.nextCardsCount, s.playStatus.getCode, s.landlord, s.fingerPrint, s.memberId, s.id.toString, "", "", ""))
  }

  def thriftClient(rpcHost: String): GameGatewayEndpoint[Future] = {
    var endpoint: GameGatewayEndpoint[Future] = map.get(rpcHost)
    endpoint == null match {
      case true => endpoint = Thrift.client.newIface[GameGatewayEndpoint[Future]](rpcHost)
        map.put(rpcHost, endpoint)
      case false =>
    }
    endpoint
  }

}
