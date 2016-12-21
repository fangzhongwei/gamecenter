package com.proto;

import com.proto.FirstProtobuf.TestBuf;

import java.io.IOException;

public class TestProtoBuf {
    public static void main(String[] args) throws IOException {
        //序列化过程
        //FirstProtobuf是生成类的名字，即proto文件中的java_outer_classname
        //testBuf是里面某个序列的名字，即proto文件中的message testBuf

        FirstProtobuf.TestBuf.Builder builder = FirstProtobuf.TestBuf.newBuilder();

        builder.setID(123);
        builder.setUrl("http://www.google.com");

        TestBuf info = builder.build();
        byte[] buf = info.toByteArray();

        TestBuf tb = TestBuf.parseFrom(buf);
        System.out.println(tb.getID());
        System.out.println(tb.getUrl());
    }
}