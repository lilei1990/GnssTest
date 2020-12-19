package com.example.demo.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * 任意三角形的余弦定理
 * 三角形ABC,设LAB=c,LAC=B,LBC=a,∠CAB为α,∠ABC为β,∠ACB为γ
 * 则      c的平方 = a的平方 + b的平方 - 2*a*b*cos(γ)
 * a的平方 = b的平方 + c的平方 - 2*a*b*cos(α)
 * b的平方 = a的平方 + c的平方 - 2*a*b*cos(β)
 *
 * @author huanguo
 */
public class MathUtils {

    /**
     * 得到A点的 夹角
     *
     * @param ac=b
     * @param ab=c
     * @param bc=a
     * @return 返回弧度
     */
    public static double getAngleA(double ac, double ab, double bc) {
        // ∠CAB = arccos((b2 + c2 - a2) / (2bc))
        double cab = Math.acos((Math.pow(ac, 2) + Math.pow(ab, 2) - Math.pow(bc, 2)) / (2 * ac * ab));
        return cab;
    }

    /**
     * 已知三角形三条边长,得到三角形相关参数
     *
     * @param ac=b
     * @param ab=c
     * @param bc=a
     * @return
     */
    public static double getHc(double b, double a, double c) {
        double space;
        if (c + b == a) {//点在线段上
            space = 0;
            return space;
        }
        if (a <= 0.000001) {//不是线段，是一个点
            space = b;
            return space;
        }
        if (c * c == a * a + b * b) { //组成直角三角形，(x1,y1)为直角
            space = b;
            return space;
        }
        if (b * b == a * a + c * c) {//组成直角三角形，(x2,y2)为直角
            space = c;
            return space;
        }
        //组成三角形，则求三角形的高
        double p = (a + b + c) / 2;// 半周长
        double temp = p * (p - a) * (p - b) * (p - c);
        if (temp >= 0) {
            double s = Math.sqrt(temp);// 海伦公式求面积
            space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
        } else {
            space = 0;
        }

        return space;
    }

    /**
     * 已知三角形三条边长,得到三角形相关参数,c在ab上的垂直点到a的距离
     *
     * @param ac
     * @param ab
     * @param bc
     * @return
     */
    public static double getHca(double ac, double ab, double bc) {
        // h = sin(∠CAB)*(b)
        double cab = getAngleA(ac, ab, bc);
        double ao = ac * Math.cos(cab);
        return ao;
    }

    /**
     * 判断角度变化是否超过最大值
     *
     * @param d1  [-180, 180]
     * @param d2  [-180, 180]
     * @param max [0, 180]
     * @return true:差值小于等于max
     * false:差值大于max
     */
    public static boolean isDegress(double d1, double d2, double max) {
        double d = d2 - d1 + 720;
        int n = (int) d / 360;
        double chg = d - n * 360;
        if (chg > 180) {
            chg = 360 - chg;
        }
        if (chg > max) {
            return false;
        }
        return true;
    }

    /**
     * 获取平均值
     *
     * @param list
     * @return
     */
    public static double averageList(List<Double> list) {
        double total = 0.0f;
        for (double d : list) {
            total += d;
        }
        return total / list.size();
    }

    /**
     * 获取均方差
     *
     * @param list
     * @param mid
     * @return
     */
    public static double squareList(List<Double> list, double avargae) {
        double square = 0.0f;
        for (double d : list) {
            square += Math.pow(d - avargae, 2);
        }
        return Math.sqrt(square / list.size());
    }


    public static double sqrtRoot(double m) {
        if (m == 0) {
            return 0;
        }

        float i = 0;
        double x1, x2 = 0;
        while ((i * i) <= m) {
            i += 0.1;
        }
        x1 = i;
        for (int j = 0; j < 10; j++) {
            x2 = m;
            x2 /= x1;
            x2 += x1;
            x2 /= 2;
            x1 = x2;
        }
        return x2;
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0X000000FF) << shift;
        }
        return value;
    }

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    // 获得一个给定范围的随机整数
    public static int getRandomNum(int smallistNum, int BiggestNum) {
        Random random = new Random();
        return (Math.abs(random.nextInt()) % (BiggestNum - smallistNum + 1)) + smallistNum;
    }

    public float formatScale(float value, int scale) {
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，
        BigDecimal bd = new BigDecimal((double) value);
        bd = bd.setScale(scale, roundingMode);
        return bd.floatValue();
    }

    /**
     * 高精度除法
     *
     * @param dividend
     * @param divisor  分母
     * @return
     */
    public static float divide(double dividend, double divisor) {
        return BigDecimal.valueOf(dividend).divide(BigDecimal.valueOf(divisor)).floatValue();
    }

    public static long parseUnsignedLong(String s, int radix)
            throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }

        int len = s.length();
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar == '-') {
                throw new
                        NumberFormatException(String.format("Illegal leading minus sign " +
                        "on unsigned string %s.", s));
            } else {
                if (len <= 12 || // Long.MAX_VALUE in Character.MAX_RADIX is 13 digits
                        (radix == 10 && len <= 18)) { // Long.MAX_VALUE in base 10 is 19 digits
                    return Long.parseLong(s, radix);
                }

                // No need for range checks on len due to testing above.
                long first = Long.parseLong(s.substring(0, len - 1), radix);
                int second = Character.digit(s.charAt(len - 1), radix);
                if (second < 0) {
                    throw new NumberFormatException("Bad digit at end of " + s);
                }
                long result = first * radix + second;
                if (compareUnsigned(result, first) < 0) {
                    /*
                     * The maximum unsigned value, (2^64)-1, takes at
                     * most one more digit to represent than the
                     * maximum signed value, (2^63)-1.  Therefore,
                     * parsing (len - 1) digits will be appropriately
                     * in-range of the signed parsing.  In other
                     * words, if parsing (len -1) digits overflows
                     * signed parsing, parsing len digits will
                     * certainly overflow unsigned parsing.
                     *
                     * The compareUnsigned check above catches
                     * situations where an unsigned overflow occurs
                     * incorporating the contribution of the final
                     * digit.
                     */
                    throw new NumberFormatException(String.format("String value %s exceeds " +
                            "range of unsigned long.", s));
                }
                return result;
            }
        } else {
            throw new NumberFormatException(s);
        }
    }

    public static int parseUnsignedInt_LE(String s, int radix) {
        if (s.length() % 2 != 0) {
            s = "0" + s;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i > 0; i -= 2) {
            sb.append(s.substring(i - 2, i));
        }
        return parseUnsignedInt(sb.toString(), radix);
    }


    public static int parseUnsignedInt(String s, int radix)
            throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }

        int len = s.length();
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar == '-') {
                throw new
                        NumberFormatException(String.format("Illegal leading minus sign " +
                        "on unsigned string %s.", s));
            } else {
                if (len <= 5 || // Integer.MAX_VALUE in Character.MAX_RADIX is 6 digits
                        (radix == 10 && len <= 9)) { // Integer.MAX_VALUE in base 10 is 10 digits
                    return Integer.parseInt(s, radix);
                } else {
                    long ell = Long.parseLong(s, radix);
                    if ((ell & 0xffff_ffff_0000_0000L) == 0) {
                        return (int) ell;
                    } else {
                        throw new
                                NumberFormatException(String.format("String value %s exceeds " +
                                "range of unsigned int.", s));
                    }
                }
            }
        } else {
            throw new NumberFormatException(s);
        }
    }

    /**
     * Compares two {@code long} values numerically treating the values
     * as unsigned.
     *
     * @param x the first {@code long} to compare
     * @param y the second {@code long} to compare
     * @return the value {@code 0} if {@code x == y}; a value less
     * than {@code 0} if {@code x < y} as unsigned values; and
     * a value greater than {@code 0} if {@code x > y} as
     * unsigned values
     * @since 1.8
     */
    public static int compareUnsigned(long x, long y) {
        return Long.compare(x + Long.MIN_VALUE, y + Long.MIN_VALUE);
    }


}
