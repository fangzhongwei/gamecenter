package com.jxjxgo.gamecenter.helper;

import com.jxjxgo.gamecenter.enumnate.CardsType;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by fangzhongwei on 2017/3/8.
 */
public class TypeWithPoints {
    private CardsType cardsType;

    private int p;

    private List<Integer> ps;

    public TypeWithPoints() {
    }

    public TypeWithPoints(CardsType cardsType) {
        this.cardsType = cardsType;
    }

    public TypeWithPoints(CardsType cardsType, int p, List<Integer> ps) {
        this.cardsType = cardsType;
        this.p = p;
        this.ps = ps;
    }

    public CardsType getCardsType() {
        return cardsType;
    }

    public void setCardsType(CardsType cardsType) {
        this.cardsType = cardsType;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public List<Integer> getPs() {
        return ps;
    }

    public void setPs(List<Integer> ps) {
        this.ps = ps;
    }

    public String generateKeys(){
        final StringBuilder sb = new StringBuilder();
        if (p != 0) {
            sb.append(p).append(',');
        }
        if (CollectionUtils.isNotEmpty(ps)) {
            for (Integer point : ps) {
                sb.append(point).append(',');
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeWithPoints that = (TypeWithPoints) o;

        if (p != that.p) return false;
        if (cardsType != that.cardsType) return false;
        return ps != null ? CardsJudgeHelper.GetInstance().join(ps).equals(CardsJudgeHelper.GetInstance().join(that.ps)) : that.ps == null;

    }

    @Override
    public int hashCode() {
        int result = cardsType != null ? cardsType.hashCode() : 0;
        result = 31 * result + p;
        result = 31 * result + (ps != null ? ps.hashCode() : 0);
        return result;
    }
}
