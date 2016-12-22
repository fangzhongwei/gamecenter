package com.lawsofnature.gamecenter.noti

/**
  * Created by fangzhongwei on 2016/12/22.
  */
case class GameStart(traceId:String, memberId: Long, cards: List[Int], dzCards: List[Int], isChooseDz: Boolean, previousUsername: String, nextUsername: String)
