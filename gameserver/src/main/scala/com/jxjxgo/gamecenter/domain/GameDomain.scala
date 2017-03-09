package com.jxjxgo.gamecenter.domain

import com.jxjxgo.gamecenter.enumnate.PlayStatus

/**
  * Created by fangzhongwei on 2017/2/12.
  */
case class RoomConfig(gameType: Int, deviceType: Int, minDiamondAmount: Int, maxDiamondAmount: Int, roomRanges: String = "", gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp, topic: String)

case class Room(id: Long, name: String = "", status: Short, minTableId: Long = 0L, maxTableId: Long = 0L, seatCapacity: Long = 0L, currentPlayers: Long = 0L, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

case class PlayTable(id: Long, name: String = "", status: Short, seatCapacity: Int = 0, minSeatId: Long = 0L, maxSeatId: Long = 0L, currentPlayers: Int, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)

case class Seat(id: Long, status: Short, memberId: Long = 0L, cards: String, landlordCards: String, proCardsInfo: String, fingerPrint: String, gameId: Long, gameType: Int, deviceType: Int, multiples: Int = 1, baseAmount: Int, landlordPosition: Char, previousNickname: String, previousCardsCount: Short, nextNickname: String, nextCardsCount: Short, playStatus: PlayStatus, seqInGame: Short, landlord: Boolean = false, socketId: Long, gmtUpdate: java.sql.Timestamp)

case class Game(id: Long, gameType: Int = 0, deviceType: Int, baseAmount: Int, multiples: Int, status: Short, player1Id: Long = 0L, player2Id: Long = 0L, player3Id: Long = 0L, seat1Id: Long = 0L, seat2Id: Long = 0L, seat3Id: Long = 0L, seqInGame: Int = 0, activePlayerId: Long = 0L, player1Cards: String = "", player2Cards: String = "", player3Cards: String = "", outsideCards: String = "", landlordId: Long, winnerId: Long, gmtCreate: java.sql.Timestamp, gmtUpdate: java.sql.Timestamp)
