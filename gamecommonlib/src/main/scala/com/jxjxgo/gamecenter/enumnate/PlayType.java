package com.jxjxgo.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2017/3/7.
 */
public enum  PlayType {
    DecideYes((short) 1, "Decide"),
    DecideNo((short) 2, "Decide"),
    Play((short) 9, "Play");

    private short code;
    private String desc;

    PlayType(short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static PlayType get(short code) {
        for(PlayType status: PlayType.values()) {
            if (status.getCode() == code) return status;
        }
        return null;
    }

    @Override
    public String toString() {
        return "PlayType{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
