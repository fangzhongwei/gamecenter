package com.jxjxgo.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/20.
 */
public enum  RoomStatus {
    Waiting(1, "Waiting"),
    Full(99, "Full");

    private int code;
    private String desc;

    RoomStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RoomStatus{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
