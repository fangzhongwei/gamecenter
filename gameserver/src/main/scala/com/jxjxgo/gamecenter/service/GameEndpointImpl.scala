package com.jxjxgo.gamecenter.service

import javax.inject.Inject

import com.jxjxgo.common.exception.{ErrorCode, ServiceException}
import com.jxjxgo.gamecenter.rpc.domain._
import com.twitter.util.Future
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by fangzhongwei on 2017/2/8.
  */
class GameEndpointImpl @Inject()(gameService: GameService) extends GameEndpoint[Future] {
  private[this] val logger: Logger = LoggerFactory.getLogger(getClass)

  override def checkGameStatus(traceId: String, request: CheckGameStatusRequest): Future[CheckGameStatusResponse] = {
    try {
      Future.value(gameService.checkGameStatus(traceId, request))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(CheckGameStatusResponse(code = ex.getErrorCode.getCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(CheckGameStatusResponse(code = ErrorCode.EC_SYSTEM_ERROR.getCode))
    }
  }

  override def joinGame(traceId: String, request: JoinGameRequest): Future[GameBaseResponse] = {
    try {
      Future.value(gameService.joinGame(traceId, request))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(GameBaseResponse(code = ex.getErrorCode.getCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(GameBaseResponse(code = ErrorCode.EC_SYSTEM_ERROR.getCode))
    }
  }

  override def playCards(traceId: String, request: PlayCardsRequest): Future[PlayCardsResponse] = {
    try {
      Future.value(gameService.playCards(traceId, request))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(PlayCardsResponse(code = ex.getErrorCode.getCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(PlayCardsResponse(code = ErrorCode.EC_SYSTEM_ERROR.getCode))
    }
  }

  override def setGameStatus(traceId: String, memberId: Long, gameStatus: String): Future[GameBaseResponse] = {
    try {
      Future.value(gameService.setGameStatus(traceId, memberId, gameStatus))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(GameBaseResponse(code = ex.getErrorCode.getCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(GameBaseResponse(code = ErrorCode.EC_SYSTEM_ERROR.getCode))
    }
  }

  override def saveChannelAddress(traceId: String, memberId: Long, host: String, addressType: String = "rpc"): Future[GameBaseResponse] = {
    try {
      Future.value(gameService.saveChannelAddress(traceId, memberId, host, addressType))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(GameBaseResponse(code = ex.getErrorCode.getCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(GameBaseResponse(code = ErrorCode.EC_SYSTEM_ERROR.getCode))
    }
  }

  override def playerOnline(traceId: String, request: OnlineRequest): Future[GameBaseResponse] = {
    try {
      Future.value(gameService.playerOnline(traceId, request))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(GameBaseResponse(code = ex.getErrorCode.getCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(GameBaseResponse(code = ErrorCode.EC_SYSTEM_ERROR.getCode))
    }
  }

  override def playerOffline(traceId: String, socketUuid: String, memberId: Long): Future[GameBaseResponse] = {
    try {
      Future.value(gameService.playerOffline(traceId, socketUuid, memberId))
    } catch {
      case ex: ServiceException =>
        logger.error(traceId, ex)
        Future.value(GameBaseResponse(code = ex.getErrorCode.getCode))
      case ex: Exception =>
        logger.error(traceId, ex)
        Future.value(GameBaseResponse(code = ErrorCode.EC_SYSTEM_ERROR.getCode))
    }
  }
}
