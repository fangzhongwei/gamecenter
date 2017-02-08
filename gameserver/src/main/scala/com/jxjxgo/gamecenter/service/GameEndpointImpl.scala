package com.jxjxgo.gamecenter.service

import javax.inject.Inject

import com.jxjxgo.game.rpc.domain._
import com.twitter.util.Future

/**
  * Created by fangzhongwei on 2017/2/8.
  */
class GameEndpointImpl @Inject()(gameService: GameService) extends GameEndpoint[Future] {
  override def checkGameStatus(traceId: String, request: CheckGameStatusRequest): Future[CheckGameStatusResponse] = {
    Future.value(gameService.checkGameStatus(traceId, request))
  }

  override def joinGame(traceId: String, request: JoinGameRequest): Future[JoinGameResponse] = {
    Future.value(gameService.joinGame(traceId, request))
  }

  override def playCards(traceId: String, request: PlayCardsRequest): Future[PlayCardsResponse] = {
    Future.value(gameService.playCards(traceId, request))
  }

  override def setGameStatus(traceId: String, memberId: Long, gameStatus: String): Future[GameBaseResponse] = {
    Future.value(gameService.setGameStatus(traceId, memberId, gameStatus))
  }
}
