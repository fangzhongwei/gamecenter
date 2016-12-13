package com.lawsofnature.gamecenter.domain;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by fangzhongwei on 2016/12/9.
 */
public class EchoInfo {
    @Protobuf
    public String message;

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EchoInfo{" +
                "message='" + message + '\'' +
                '}';
    }
}