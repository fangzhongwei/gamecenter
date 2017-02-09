package com.jxjxgo.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/17.
 */
public enum OnlineMemberGameStatus {
    Idel(GameType.None, false, "Idel"),

    Waiting1010(GameType.T1010, false, "Waiting1010"),
    Playing1010(GameType.T1010, true, "Playing1010"),

    Waiting1020(GameType.T1020, false, "Waiting1020"),
    Playing1020(GameType.T1020, true, "Playing1020"),

    Waiting1050(GameType.T1050, false, "Waiting1050"),
    Playing1050(GameType.T1050, true, "Playing1050"),

    Waiting1100(GameType.T1100, false, "Waiting1100"),
    Playing1100(GameType.T1100, true, "Playing1100"),

    Waiting1200(GameType.T1200, false, "Waiting1200"),
    Playing1200(GameType.T1200, true, "Playing1200"),

    Waiting1500(GameType.T1500, false, "Waiting1500"),
    Playing1500(GameType.T1500, true, "Playing1500");

    private GameType type;
    private boolean inPlaying;
    private String code;

    OnlineMemberGameStatus(GameType type, boolean inPlaying, String code) {
        this.type = type;
        this.inPlaying = inPlaying;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static OnlineMemberGameStatus get(GameType gameType, boolean inPlaying) {
        for (OnlineMemberGameStatus status : OnlineMemberGameStatus.values()) {
            if (status.getType() == gameType && status.isInPlaying() == inPlaying) return status;
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OnlineMemberGameStatus{");
        sb.append("type=").append(type);
        sb.append(", inPlaying=").append(inPlaying);
        sb.append(", code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
