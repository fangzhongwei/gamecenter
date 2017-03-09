package com.jxjxgo.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2017/3/5.
 */
public enum PlayStatus {

    WaitingStart("WS", "WaitingStart"),
    DecideToBeLandlord("DTBL", "DecideToBeLandlord"),
    WaitingOtherPlay("WOP", "WaitingOtherPlay"),
    TurnToPlay("TTP", "TurnToPlay"),
    End("END", "End");

    private String code;
    private String desc;

    PlayStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static PlayStatus get(String code) {
        for (PlayStatus s : PlayStatus.values()) {
            if (s.getCode().equals(code)) return s;
        }
        return null;
    }
}
