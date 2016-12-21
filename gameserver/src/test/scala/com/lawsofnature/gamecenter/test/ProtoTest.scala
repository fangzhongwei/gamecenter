package com.lawsofnature.gamecenter.test

import java.nio.charset.StandardCharsets
import java.util

import com.lawsofnature.common.helper.GZipHelper

/**
  * Created by fangzhongwei on 2016/12/13.
  */
object ProtoTest extends App{
//  val byteArray = Files.readAllBytes(Paths.get("C:\\Users\\fangzhongwei\\RiderProjects\\ConsoleApplication\\ConsoleApplication\\bin\\Release\\TestBuf.dat"))
//
//  val tb: FirstProtobuf.TestBuf = TestBuf.parseFrom(byteArray)
//  System.out.println(tb.getID)
//  System.out.println(tb.getUrl)
//
//  System.out.println(util.Arrays.toString(tb.toByteArray))
  private val orginalArray: Array[Byte] = "aaaaaabbbbbbbbcccccccddddddffff中国11111".getBytes(StandardCharsets.UTF_8)
  println(util.Arrays.toString(orginalArray))
  private val compress: Array[Byte] = GZipHelper.compress(orginalArray)
  println(util.Arrays.toString(compress))
}
