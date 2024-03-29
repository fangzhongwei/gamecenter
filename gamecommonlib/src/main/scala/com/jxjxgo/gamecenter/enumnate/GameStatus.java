package com.jxjxgo.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/21.
 */
public enum GameStatus {
    WaitingLandlord((short) 1, "WaitingLandlord"),
    Aborted((short) -1, "Aborted"),
    Playing((short) 9, "Playing"),
    Finished((short) 19, "Finished"),
    Settled((short) 99, "Settled");

    private short code;
    private String desc;

    GameStatus(short code, String desc) {
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

    public static GameStatus get(short code) {
        for(GameStatus status: GameStatus.values()) {
            if (status.getCode() == code) return status;
        }
        return null;
    }

    @Override
    public String toString() {
        return "GameStatus{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
