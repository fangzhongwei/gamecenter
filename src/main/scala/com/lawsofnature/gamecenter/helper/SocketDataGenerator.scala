package com.lawsofnature.gamecenter.helper

import com.lawsofnature.common.edecrypt.DESUtils
import com.lawsofnature.common.helper.{ByteHelper, GZipHelper}

/**
  * Created by fangzhongwei on 2016/12/14.
  */
object SocketDataGenerator {
  def generate(actionType: Int, array: Array[Byte], key: String): Array[Byte] = {
    val actionTypeByteArray: Array[Byte] = ByteHelper.intToByteArray(actionType)
    val compress: Array[Byte] = GZipHelper.compress(array)
    val encrypt: Array[Byte] = DESUtils.encrypt(compress, key)
    val dataLengthArray: Array[Byte] = ByteHelper.intToByteArray(encrypt.length)
    ByteHelper.combineTowBytes(ByteHelper.combineTowBytes(actionTypeByteArray, dataLengthArray), encrypt)
  }
}
