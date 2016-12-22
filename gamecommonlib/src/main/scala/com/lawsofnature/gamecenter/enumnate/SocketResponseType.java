package com.lawsofnature.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/13.
 */
public enum SocketResponseType {
    GameStart(1,"GameStart");

    private int code;
    private String desc;

    SocketResponseType(int code, String desc) {
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

    public static SocketResponseType get(int code) {
        for (SocketResponseType type : SocketResponseType.values()) {
            if (type.getCode() == code) return type;
        }
        return null;
    }

    @Override
    public String toString() {
        return "SocketResponseType{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
