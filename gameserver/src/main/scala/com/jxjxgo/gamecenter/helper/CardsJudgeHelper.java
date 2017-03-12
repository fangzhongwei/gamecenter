package com.jxjxgo.gamecenter.helper;


import com.jxjxgo.common.exception.ErrorCode;
import com.jxjxgo.common.exception.ServiceException;
import com.jxjxgo.gamecenter.enumnate.CardsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.jxjxgo.gamecenter.enumnate.CardsType.*;

/**
 * Created by fangzhongwei on 2017/3/8.
 */
public class CardsJudgeHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardsJudgeHelper.class);
    private static final CardsJudgeHelper instance = new CardsJudgeHelper();

    private CardsJudgeHelper() {

    }

    public static CardsJudgeHelper GetInstance() {
        return instance;
    }

    private TypeWithPoints tpInvalid = new TypeWithPoints(Invalid);

    public TypeWithPoints judgeType(List<Integer> list) {
        LOGGER.info("JudgeType, points:" + join(list));
        //plain list
        List<Integer> points = new ArrayList<Integer>(list.size());
        for (int i = 0; i < list.size(); i++) {
            points.add(list.get(i) % 100);
        }
        LOGGER.info("Plain points:" + join(points));

        Collections.sort(points, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1.intValue() > o2) {
                    return 1;
                } else if (o1.intValue() > o2) {
                    return 1;
                }
                return 0;
            }
        });
        LOGGER.info("Points after sort:" + join(points));
        TypeWithPoints tp = new TypeWithPoints();

        if (isSingle(points)) {
            tp.setCardsType(Single);
            tp.setP(points.get(0));
            return tp;
        }

        if (isDouble(points)) {
            tp.setCardsType(Doub);
            tp.setP(points.get(0));
            return tp;
        }

        if (points.size() == 2 && points.get(0) == 16 && points.get(1) == 17) {
            tp.setCardsType(DoubJoker);
            return tp;
        }

        if (points.size() == 4 && points.get(0) == points.get(3)) {
            tp.setCardsType(DoubJoker);
            tp.setP(points.get(0));
            return tp;
        }

        if (isSeq(points)) {
            tp.setCardsType(Seq);
            tp.setPs(points);
            return tp;
        }

        List<Integer> doubSeq = judgeDoubSeq(points);
        if (doubSeq != null) {
            tp.setCardsType(DoubSeq);
            tp.setPs(doubSeq);
            return tp;
        }

        List<Integer> threeSeq = judgeThreeSeq(points);
        if (threeSeq != null) {
            tp.setCardsType(ThreeSeq);
            tp.setPs(threeSeq);
            return tp;
        }

        List<Integer> threeWithOneSeq = judgeThreeWithOneSeq(points);
        if (threeWithOneSeq != null) {
            tp.setCardsType(ThreeWithOneSeq);
            tp.setPs(threeWithOneSeq);
            return tp;
        }

        List<Integer> threeWithTwoSeq = judgeThreeWithTwoSeq(points);
        if (threeWithTwoSeq != null) {
            tp.setCardsType(ThreeWithTwoSeq);
            tp.setPs(threeWithTwoSeq);
            return tp;
        }

        List<Integer> fourWith2SingleSeq = judgeFourWith2SingleSeq(points);
        if (fourWith2SingleSeq != null) {
            tp.setCardsType(FourWith2SingleSeq);
            tp.setPs(fourWith2SingleSeq);
            return tp;
        }

        List<Integer> fourWith2DoubSeq = judgeFourWith2DoubSeq(points);
        if (fourWith2DoubSeq != null) {
            tp.setCardsType(FourWith2DoubSeq);
            tp.setPs(fourWith2DoubSeq);
            return tp;
        }

        return tpInvalid;
    }

    private Boolean isSingle(List<Integer> points) {
        return points.size() == 1;
    }

    private Boolean isDouble(List<Integer> points) {
        return points.get(0) == points.get(1);
    }

    private Boolean isSeq(List<Integer> points) {
        if (points.size() < 5 || points.size() > 12) {
            return false;
        }
        if (points.get(points.size() - 1) > 14) {
            return false;
        }
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i + 1) - points.get(i) != 1) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> judgeDoubSeq(List<Integer> points) {
        if (points.size() < 6 || points.size() > 20) {
            return null;
        }
        if (points.size() % 2 != 0) {
            return null;
        }
        if (points.get(points.size() - 1) > 14) {
            return null;
        }
        List<Integer> kp = new ArrayList<Integer>(points.size() / 2);
        for (int i = 3; i < points.size(); i += 2) {
            if (points.get(i - 3) != points.get(i - 2) || points.get(i - 2) - points.get(i - 1) != 1 ||
                    points.get(i - 1) != points.get(i)) {
                return null;
            }
            if (i == 3) {
                kp.add(points.get(1));
            }
            kp.add(points.get(i));
        }
        return kp;
    }

    private List<Integer> judgeThreeSeq(List<Integer> points) {
        if (points.size() % 3 != 0) {
            return null;
        }
        List<Integer> ps = new ArrayList<Integer>();
        for (int i = 2; i < points.size(); i += 3) {
            if (points.get(i - 2) != points.get(i - 1) || points.get(i - 1) != points.get(i)) {
                return null;
            }
            ps.add(points.get(i));
        }

        if (points.size() / 3 != ps.size()) {
            return null;
        }

        for (int i = 0; i < ps.size() - 1; i++) {
            if (ps.get(i) + 1 != ps.get(i + 1)) {
                return null;
            }
        }
        return ps;
    }

    private List<Integer> judgeThreeWithOneSeq(List<Integer> points) {
        if (points.size() % 4 != 0) {
            return null;
        }
        List<Integer> ps = new ArrayList<Integer>();
        for (int i = 2; i < points.size(); i++) {
            if (points.get(i - 2) == points.get(i)) {
                if (i == points.size() - 1 && points.get(i - 3) != points.get(i - 2)) {
                    ps.add(points.get(i));
                } else if (i == 2 && points.get(i) != points.get(i + 1)) {
                    ps.add(points.get(i));
                } else {
                    if (points.get(i - 3) != points.get(i - 2) && points.get(i) != points.get(i + 1)) {
                        ps.add(points.get(i));
                    }
                }
            }
        }
        if (points.size() / 4 != ps.size()) {
            return null;
        }
        for (int i = 0; i < ps.size() - 1; i++) {
            if (ps.get(i) + 1 != ps.get(i + 1)) {
                return null;
            }
        }
        return ps;
    }

    private List<Integer> judgeThreeWithTwoSeq(List<Integer> points) {
        if (points.size() % 5 != 0) {
            return null;
        }
        List<Integer> ps3 = new ArrayList<Integer>();
        for (int i = 2; i < points.size(); i++) {
            if (points.get(i - 2) == points.get(i)) {
                if (i == points.size() - 1 && points.get(i - 3) != points.get(i - 2)) {
                    ps3.add(points.get(i));
                } else if (i == 2 && points.get(i) != points.get(i + 1)) {
                    ps3.add(points.get(i));
                } else {
                    if (points.get(i - 3) != points.get(i - 2) && points.get(i) != points.get(i + 1)) {
                        ps3.add(points.get(i));
                    }
                }
            }
        }

        if (points.size() / 5 != ps3.size()) {
            return null;
        }

        for (int i = 0; i < ps3.size() - 1; i++) {
            if (ps3.get(i) + 1 != ps3.get(i + 1)) {
                return null;
            }
        }

        List<Integer> ps2 = new ArrayList<Integer>();
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i - 1) == points.get(i)) {
                if (i == points.size() - 1 && points.get(i - 2) != points.get(i - 1)) {
                    ps2.add(points.get(i));
                } else if (i == 1 && points.get(i) != points.get(i + 1)) {
                    ps2.add(points.get(i));
                } else {
                    if (points.get(i - 2) != points.get(i - 1) && points.get(i) != points.get(i + 1)) {
                        ps2.add(points.get(i));
                    }
                }
            }
        }

        if (points.size() / 5 != ps2.size()) {
            return null;
        }

        return ps3;
    }

    private List<Integer> judgeFourWith2SingleSeq(List<Integer> points) {
        if (points.size() % 6 != 0) {
            return null;
        }
        List<Integer> ps = new ArrayList<Integer>();
        for (int i = 3; i < points.size(); i++) {
            if (points.get(i - 3) == points.get(i)) {
                ps.add(points.get(i));
            }
        }
        if (points.size() / 6 != ps.size()) {
            return null;
        }
        for (int i = 0; i < ps.size() - 1; i++) {
            if (ps.get(i) + 1 != ps.get(i + 1)) {
                return null;
            }
        }
        return ps;
    }

    private List<Integer> judgeFourWith2DoubSeq(List<Integer> points) {
        if (points.size() % 8 != 0) {
            return null;
        }
        List<Integer> ps4 = new ArrayList<Integer>();
        for (int i = 3; i < points.size(); i++) {
            if (points.get(i - 3) == points.get(i)) {
                ps4.add(points.get(i));
            }
        }
        if (points.size() / 8 != ps4.size()) {
            return null;
        }
        for (int i = 0; i < ps4.size() - 1; i++) {
            if (ps4.get(i) + 1 != ps4.get(i + 1)) {
                return null;
            }
        }

        List<Integer> ps2 = new ArrayList<Integer>();
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i - 1) == points.get(i)) {
                if (i == points.size() - 1 && points.get(i - 2) != points.get(i - 1)) {
                    ps2.add(points.get(i));
                } else if (i == 1 && points.get(i) != points.get(i + 1)) {
                    ps2.add(points.get(i));
                } else {
                    if (points.get(i - 2) != points.get(i - 1) && points.get(i) != points.get(i + 1)) {
                        ps2.add(points.get(i));
                    }
                }
            }
        }

        if (points.size() / 4 != ps2.size()) {
            return null;
        }
        return ps4;
    }

    public void sort(List<Integer> points) {
        Collections.sort(points, new Comparator<Integer>() {
            @Override
            public int compare(Integer point1, Integer point2) {

                int p1 = point1 % 100;
                int p2 = point2 % 100;
                if (p1 > p2) {
                    return 1;
                }
                if (p1 < p2) {
                    return -1;
                }
                if (point1 > point2) {
                    return 1;
                }
                if (point1 < point2) {
                    return -1;
                }
                return 0;

            }
        });
    }

    public String join(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        final StringBuilder j = new StringBuilder();
        for (int i : list) {
            j.append(i).append(',');
        }
        return j.substring(0, j.length() - 1);
    }


    public boolean canPlay(TypeWithPoints twp, TypeWithPoints typeWithPoints) {
        CardsType originalType = twp.getCardsType();
        CardsType readyType = typeWithPoints.getCardsType();

        if (originalType == CardsType.Invalid || originalType == CardsType.Pass || readyType == CardsType.Invalid || readyType == CardsType.Exist)
        {
            throw ServiceException.make(ErrorCode.EC_GAME_INVALID_REQUEST_DATA);
        }

        switch (originalType) {
            case Exist:
                return !(readyType == Invalid || readyType == Exist || readyType == Pass);
            case Single:
                if (readyType == DoubJoker || readyType == Four) {
                    return true;
                }
                if (readyType == Single) {
                    return typeWithPoints.getP() > twp.getP();
                }
                return false;
            case Doub:
                if (readyType == DoubJoker || readyType == Four) {
                    return true;
                }
                if (readyType == Doub) {
                    return typeWithPoints.getP() > twp.getP();
                }
                return false;
            case Four:
                if (readyType == DoubJoker) {
                    return true;
                }
                if (readyType == Four) {
                    return typeWithPoints.getP() > twp.getP();
                }
                return false;
            case DoubJoker:
                return false;
            default:
                if (readyType == DoubJoker || readyType == Four) {
                    return true;
                }
                if (readyType == originalType) {
                    List<Integer> oldList = typeWithPoints.getPs();
                    List<Integer> newList = twp.getPs();
                    return oldList.size() == newList.size() && oldList.get(newList.size() - 1) < newList.get(newList.size() - 1);
                }
                return false;
        }
    }
}
