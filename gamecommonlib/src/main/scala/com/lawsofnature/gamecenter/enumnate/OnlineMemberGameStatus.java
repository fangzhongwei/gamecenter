package com.lawsofnature.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/17.
 */
public enum OnlineMemberGameStatus {
    Idel(GameType.None, false, ""),

    Waiting1000(GameType.T1001, false, ""),
    Playing1000(GameType.T1001, true, ""),

    Waiting2000(GameType.T1001, false, ""),
    Playing2000(GameType.T1001, true, ""),

    Waiting4000(GameType.T1001, false, ""),
    Playing4000(GameType.T1001, true, ""),

    Waiting8000(GameType.T1001, false, ""),
    Playing8000(GameType.T1001, true, ""),

    Waiting20000(GameType.T1001, false, ""),
    Playing20000(GameType.T1001, true, "");

    private GameType type;
    private boolean inPlaying;
    private String desc;

    OnlineMemberGameStatus(GameType type, boolean inPlaying, String desc) {
        this.type = type;
        this.inPlaying = inPlaying;
        this.desc = desc;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public boolean isInPlaying() {
        return inPlaying;
    }

    public void setInPlaying(boolean inPlaying) {
        this.inPlaying = inPlaying;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static OnlineMemberGameStatus get(GameType gameType, boolean inPlaying) {
        for (OnlineMemberGameStatus status : OnlineMemberGameStatus.values()) {
            if (status.getType() == gameType && status.isInPlaying() == inPlaying) return status;
        }
        return null;
    }

    @Override
    public String toString() {
        return "OnlineMemberGameStatus{" +
                "type=" + type +
                ", desc='" + desc + '\'' +
                '}';
    }
}
