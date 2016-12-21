package com.lawsofnature.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/17.
 */
public enum GameType {

    None(0, ""),

    T1001(1000, ""),

    T1002(2000, ""),

    T1004(4000, ""),

    T1008(8000, ""),

    T1020(20000, "");

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
