package com.jxjxgo.gamecenter.helper

import scala.util.Random

/**
  * Created by fangzhongwei on 2016/12/21.
  */
object CardsHelper {
  private[this] val cards = (101 to 113).toList ::: (201 to 213).toList ::: (301 to 313).toList ::: (401 to 413).toList ::: (1001 to 1002).toList

  def initCards(): (List[Int], List[Int], List[Int], List[Int]) = {
    val shuffledCards: List[Int] = Random.shuffle(cards)
    (shuffledCards.slice(0, 17).sorted, shuffledCards.slice(17, 34).sorted, shuffledCards.slice(34, 51).sorted, shuffledCards.slice(51, 54).sorted)
  }
}
