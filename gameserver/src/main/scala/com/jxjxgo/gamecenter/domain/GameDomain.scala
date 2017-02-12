package com.jxjxgo.gamecenter.domain

/**
  * Created by fangzhongwei on 2017/2/12.
  */
case class RoomConfig(gameType: Int, deviceType: Int, minDiamondAmount: Int, maxDiamondAmount: Int, roomRanges: String = "", gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp, topic: String)

case class Room(id: Long, name: String = "", status: Short, minTableId: Long = 0L, maxTableId: Long = 0L, seatCapacity: Long = 0L, currentPlayers: Long = 0L, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

case class PlayTable(id: Long, name: String = "", status: Short, seatCapacity: Int = 0, minSeatId: Long = 0L, maxSeatId: Long = 0L, currentPlayers: Int, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

case class Seat(id: Long, traceId: String, status: Short, memberId: Long = 0L, cards: String, landlordCards: String, fingerPrint: String, gameId: Long, gameType: Int, deviceType: Int, multiples: Int, baseAmount: Int, previousNickname: String, previousCardsCount: Short, nextNickname: String, nextCardsCount: Short, choosingLandlord: Boolean = false, landlord: Boolean = false, turnToPlay: Boolean = false, socketId: Long, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)