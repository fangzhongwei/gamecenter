package com.jxjxgo.gamecenter.helper

import scala.util.Random

/**
  * Created by fangzhongwei on 2016/12/21.
  */
object CardsHelper {
  private[this] val cards = (103 to 115).toList ::: (203 to 215).toList ::: (303 to 315).toList ::: (403 to 415).toList ::: (516 to 517).toList

  def initCards(): (List[Int], List[Int], List[Int], List[Int]) = {
    val shuffledCards: List[Int] = Random.shuffle(cards)
    (shuffledCards.slice(0, 17).sorted, shuffledCards.slice(17, 34).sorted, shuffledCards.slice(34, 51).sorted, shuffledCards.slice(51, 54).sorted)
  }
}
