package com.jxjxgo.gamecenter.helper

import com.jxjxgo.common.helper.GZipHelper
import com.lawsofnature.common.edecrypt.DESUtils

/**
  * Created by fangzhongwei on 2016/12/14.
  */
object SocketDataGenerator {
  def generate(array: Array[Byte], key: String): Array[Byte] = {
    DESUtils.encrypt(GZipHelper.compress(array), key)
  }
}
