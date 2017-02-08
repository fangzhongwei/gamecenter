package com.jxjxgo.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/17.
 */
public enum GameType {
    None(0, ""),

    T1010(10, ""),

    T1020(20, ""),

    T1050(50, ""),

    T1100(100, ""),

    T1200(200, ""),

    T1500(500, "");

    private int code;
    private String desc;

    GameType(int code, String desc) {
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

    public static GameType get(int code) {
        for (GameType type : GameType.values()) {
            if (type.getCode() == code) return type;
        }
        return null;
    }
}
