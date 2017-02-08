package com.jxjxgo.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/21.
 */
public enum GameStatus {
    Playing((byte)1, "Playing"),
    Finished((byte)99, "Finished");

    private byte code;
    private String desc;

    GameStatus(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
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
        return "GameStatus{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
