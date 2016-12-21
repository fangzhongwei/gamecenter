package com.lawsofnature.gamecenter.enumnate;

/**
 * Created by fangzhongwei on 2016/12/13.
 */
public enum ReadStep {
    ActionType(1, true, 4, 2),
    DataLength(2, true, 4, 3),
    DataContent(3, false, 0, 1);

    private int code;
    private boolean isFixedLength;
    private int fixedLength;
    private int nextCode;

    ReadStep(int code, boolean isFixedLength, int fixedLength, int nextCode) {
        this.code = code;
        this.isFixedLength = isFixedLength;
        this.fixedLength = fixedLength;
        this.nextCode = nextCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isFixedLength() {
        return isFixedLength;
    }

    public void setFixedLength(boolean fixedLength) {
        isFixedLength = fixedLength;
    }

    public int getFixedLength() {
        return fixedLength;
    }

    public void setFixedLength(int fixedLength) {
        this.fixedLength = fixedLength;
    }

    public int getNextCode() {
        return nextCode;
    }

    public void setNextCode(int nextCode) {
        this.nextCode = nextCode;
    }

    public static ReadStep get(int code) {
        for (ReadStep step : ReadStep.values()) {
            if (step.getCode() == code) return step;
        }
        return null;
    }

    @Override
    public String toString() {
        return "ReadStep{" +
                "code=" + code +
                ", isFixedLength=" + isFixedLength +
                ", fixedLength=" + fixedLength +
                ", nextCode=" + nextCode +
                '}';
    }
}
