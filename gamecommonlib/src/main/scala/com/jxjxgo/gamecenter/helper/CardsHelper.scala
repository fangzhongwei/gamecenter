package com.jxjxgo.gamecenter.helper

import scala.util.Random

/**
  * Created by fangzhongwei on 2016/12/21.
  */
object CardsHelper {
  private[this] val cards = (103 to 115).toList ::: (203 to 215).toList ::: (303 to 315).toList ::: (403 to 415).toList ::: (516 to 517).toList

  def initCards(): (List[Int], List[Int], List[Int], List[Int]) = {
    val shuffledCards: List[Int] = Random.shuffle(cards)
    val list1: List[Int] = shuffledCards.slice(0, 17)
    val list2: List[Int] = shuffledCards.slice(17, 34)
    val list3: List[Int] = shuffledCards.slice(34, 51)
    val landlordList: List[Int] = shuffledCards.slice(51, 54)

    list1.sortWith(sortFunc)

    (list1, list2, list3, landlordList)
  }

  def sortFunc(point1:Int, point2:Int):Boolean = {
    val p1: Int = point1 % 100
    val p2: Int = point2 % 100
    if (p1 > p2) return true
    if (p1 < p2) return false
    if (point1 > point2) return true
    if (point1 < point2) return false
    return false
  }
}
