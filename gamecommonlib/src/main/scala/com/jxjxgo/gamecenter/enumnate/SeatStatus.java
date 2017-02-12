package com.jxjxgo.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/20.
 */
public enum SeatStatus {
    Idle(0, "Idle"),
    WaitingStart(1, "WaitingStart"),
    Dropped(-1, "Dropped"),
    Playing(99, "Playing");

    private int code;
    private String desc;

    SeatStatus(int code, String desc) {
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

    public static SeatStatus get(short code) {
        for (SeatStatus status: SeatStatus.values()) {
            if (status.getCode() == code) return status;
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SeatStatus{");
        sb.append("code=").append(code);
        sb.append(", desc='").append(desc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
