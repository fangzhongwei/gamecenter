package com.lawsofnature.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/13.
 */
public enum SocketActionType {
    Login(1,"login"),
    Join(2,"join"),
    PlayCards(3,"play");

    private int code;
    private String desc;

    SocketActionType(int code, String desc) {
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

    public static SocketActionType get(int code) {
        for (SocketActionType type : SocketActionType.values()) {
            if (type.getCode() == code) return type;
        }
        return null;
    }

    @Override
    public String toString() {
        return "SocketActionType{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
