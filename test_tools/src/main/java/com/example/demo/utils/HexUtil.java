package com.example.demo.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by liaozhankun on 2017/11/15.
 */

public class HexUtil {
    private final static char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 字符串输出,输入：[0x30, 0x31, 0x32, 0x33]输出：0123
     *
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes) {
        String str;
        try {
            str = new String(bytes, "UTF-8");
            return str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 字符串输出,输入：[0x30, 0x31, 0x32, 0x33]输出：30 31 32 33
     *
     * @return
     */
    public static String toHexString(byte[] data, boolean addSpace) {
        if (data == null || data.length < 1)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(data[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
            if (addSpace)
                sb.append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * 字符串输出,输入：[0x30, 0x31, 0x32, 0x33]输出：30 31 32 33
     *
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        return toHexString(bytes, false);
    }

    /**
     * 字符串输出,输入：[0x30, 0x31, 0x32, 0x33]输出：33   32  31  30
     *
     * @param bytes
     * @return
     */
    public static String toHexHostString(byte[] bytes) {
        byte v;
        int x = 0;
        int e = bytes.length;
        char[] ret = new char[3 * e];
        for (int i = e - 1; i >= 0; --i) {
            v = bytes[i];
            ret[x++] = HEX[0x0F & (v >> 4)];
            ret[x++] = HEX[0x0F & v];
            ret[x++] = ' ';
        }
        return new String(ret);
    }

    /**
     * 字符串输出,输入：[0x30, 0x31, 0x32, 0x33]输出：30 31 32 33
     *
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes, int length) {
        byte v;
        int x = 0;
        int e = length;
        char[] ret = new char[3 * e];
        for (int i = 0; i < e; ++i) {
            v = bytes[i];
            ret[x++] = HEX[0x0F & (v >> 4)];
            ret[x++] = HEX[0x0F & v];
            ret[x++] = ' ';
        }
        return new String(ret);
    }

    /**
     * 将value转成4字节数据:大端，value = 0x01020304则输出[1,2,3,4]
     *
     * @param bytes
     * @param from
     * @param value
     */
    public static boolean toBytes(byte[] bytes, int from, int value) {
        if (bytes.length < (from + 4)) {
            return false;
        }
        bytes[from + 3] = (byte) (value & 0xFF);
        bytes[from + 2] = (byte) (value >> 8 & 0xFF);
        bytes[from + 1] = (byte) (value >> 16 & 0xFF);
        bytes[from] = (byte) (value >> 24 & 0xFF);
        return true;
    }

    /**
     * 将value转成4字节数据:小端，value = 0x01020304则输出[4,3,2,1]
     *
     * @param bytes
     * @param from
     * @param value
     */
    public static boolean toBytesHost(byte[] bytes, int from, int value) {
        if (bytes.length < (from + 4)) {
            return false;
        }
        bytes[from] = (byte) (value & 0xFF);
        bytes[from + 1] = (byte) (value >> 8 & 0xFF);
        bytes[from + 2] = (byte) (value >> 16 & 0xFF);
        bytes[from + 3] = (byte) (value >> 24 & 0xFF);
        return true;
    }

    /**
     * 将value转成4字节数据:小端，value = 0x01020304则输出[4,3,2,1]
     * value = 0x0102则输出[2,1]
     *
     * @param bytes
     * @param from
     * @param value
     */
    public static boolean toBytesHost(byte[] bytes, int from, long value, int length) {
        if (bytes.length < (from + length)) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            bytes[from + i] = (byte) ((value >> (8 * i)) & 0xFF);
        }
        return true;
    }

    /**
     * 把数组转成int类型（4字节）,大端格式
     *
     * @param bytes
     * @param from
     * @return
     */
    public static int toInteger(byte[] bytes, int from) {
        int crc = 0;
        if (bytes.length < (from + 4)) {
            return crc;
        }
        crc = ((bytes[from] << 24) & 0xFF000000) +
                ((bytes[from + 1] << 16) & 0xFF0000) +
                ((bytes[from + 2] << 8) & 0xFF00) +
                (bytes[from + 3] & 0xFF);
        return crc;
    }

    /**
     * 把数组转成int类型（4字节）,小端格式:输入[1,2,3,4]输出0x04030201
     *
     * @param bytes
     * @param from
     * @return
     */
    public static int toInteger_LE(byte[] bytes, int from) {
        int crc = 0;
        if (bytes.length < (from + 4)) {
            return crc;
        }
        if (bytes.length >= 4) {
            crc += (bytes[from + 3] << 24) & 0xFF000000;
        }
        if (bytes.length >= 3) {
            crc += (bytes[from + 2] << 16) & 0xFF0000;
        }
        if (bytes.length >= 2) {
            crc += (bytes[from + 1] << 8) & 0xFF00;
        }
        if (bytes.length >= 1) {
            crc += bytes[from] & 0xFF;
        }
        return crc;
    }

    /**
     * 把数组转成int类型（4字节）,小端格式:输入[1,2,3,4]输出0x04030201
     *
     * @param bytes
     * @return
     */
    public static long toUnsignedLong_LE( byte[] bytes) {
        byte[] data = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            data[i] = bytes[bytes.length - 1 - i];
        }
        return MathUtils.parseUnsignedLong(toHexString(data), 16);
    }

    /**
     * 将double类型的数据转成points位的字符串格式,小数位截取
     * 要求：入参确保d<10的points方
     *
     * @param d
     * @param points
     * @return
     */
    public static String toString(double d, int points) {
        String value = String.valueOf(d);
        if (value.length() < points) {
            return value + String.format("%1$0" + (points - value.length()) + "d", 0);
        }
        return value.substring(0, points);
    }

    /**
     * 将16进制的string转成byte,“31323334” -> [0x31, 0x32, 0x33, 0x34]
     *
     * @param str
     * @return
     */
    public static byte[] hexStringtoBytes(String str) {
        if (0 != str.length() % 2) {
            return null;
        }
        int value;
        byte[] bts = new byte[str.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            value = Integer.parseInt(str.substring(2 * i, 2 * i + 1));
            bts[i] = (byte) (value & 0x0F);
            value = Integer.parseInt(str.substring(2 * i + 1, 2 * i + 2));
            bts[i] = (byte) (bts[i] << 4 | (value & 0x0F));
        }
        return bts;
    }

    /**
     * 转成小端数据
     * 0x01 -> [0x01]
     * 0x0102 -> [0x02, 0x01]
     * 0x01020304 -> [0x04, 0x03, 0x02, 0x01]
     *
     * @param value
     * @param length
     * @return
     */
    public static byte[] toBytes(int value, int length) {
        byte[] bts = new byte[length];
        if (1 == length) {
            bts[0] = (byte) (value & 0xFF);
        } else if (2 == length) {
            bts[0] = (byte) (value & 0xFF);
            bts[1] = (byte) (value >>> 8 & 0xFF);
        } else if (4 == length) {
            bts[0] = (byte) (value & 0xFF);
            bts[1] = (byte) (value >>> 8 & 0xFF);
            bts[2] = (byte) (value >>> 16 & 0xFF);
            bts[3] = (byte) (value >>> 24 & 0xFF);
        }
        return bts;
    }

    public static String shortToString(short[] info) {
        char ch;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < info.length; i++) {
            ch = (char) (info[i] & 0xFF);
            if (0 == ch) {
                break;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    //10进制转16进制
    public static String LongToHex(long n) {
        char[] ch = new char[20];
        int nIndex = 0;
        while (nIndex < 8) {
            long m = n / 16;
            long k = n % 16;
            if (k == 15)
                ch[nIndex] = 'F';
            else if (k == 14)
                ch[nIndex] = 'E';
            else if (k == 13)
                ch[nIndex] = 'D';
            else if (k == 12)
                ch[nIndex] = 'C';
            else if (k == 11)
                ch[nIndex] = 'B';
            else if (k == 10)
                ch[nIndex] = 'A';
            else
                ch[nIndex] = (char) ('0' + k);
            nIndex++;
            n = m;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(ch, 0, nIndex);
        sb.reverse();

        return sb.toString();
    }

    //16进制转10进制
    public static long HexToLong(String strHex) {
        long nResult = 0;
        if (!IsHex(strHex))
            return nResult;
        String str = strHex.toUpperCase();
        if (str.length() > 2) {
            if (str.charAt(0) == '0' && str.charAt(1) == 'X') {
                str = str.substring(2);
            }
        }
        int nLen = str.length();
        for (int i = 0; i < nLen; ++i) {
            char ch = str.charAt(nLen - i - 1);
            try {
                nResult += (GetHex(ch) * GetPower(16, i));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return nResult;
    }

    //计算16进制对应的数值
    public static long GetHex(char ch) throws Exception {
        if (ch >= '0' && ch <= '9')
            return (int) (ch - '0');
        if (ch >= 'a' && ch <= 'f')
            return (int) (ch - 'a' + 10);
        if (ch >= 'A' && ch <= 'F')
            return (int) (ch - 'A' + 10);
        throw new Exception("error param");
    }

    //计算幂
    public static long GetPower(long nValue, long nCount) throws Exception {
        if (nCount < 0)
            throw new Exception("nCount can't small than 1!");
        if (nCount == 0)
            return 1;
        long nSum = 1;
        for (long i = 0; i < nCount; ++i) {
            nSum = nSum * nValue;
        }
        return nSum;
    }

    //判断是否是16进制数
    public static boolean IsHex(String strHex) {
        int i = 0;
        if (strHex.length() > 2) {
            if (strHex.charAt(0) == '0' && (strHex.charAt(1) == 'X' || strHex.charAt(1) == 'x')) {
                i = 2;
            }
        }
        for (; i < strHex.length(); ++i) {
            char ch = strHex.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f'))
                continue;
            return false;
        }
        return true;
    }

    /**
     * byte转换成小端字符
     *
     * @param bytes
     * @return
     */
    public static String bytesToString_LE(byte[] bytes) {
        byte v;
        int x = 0;
        int e = bytes.length;
        char[] ret = new char[2 * e];
        for (int i = e - 1; i >= 0; --i) {
            v = bytes[i];
            ret[x++] = HEX[0x0F & (v >> 4)];
            ret[x++] = HEX[0x0F & v];
        }
        return new String(ret);
    }

    public static String byteToString(byte v) {
        char[] ret = new char[2];
        ret[0] = HEX[0x0F & (v >> 4)];
        ret[1] = HEX[0x0F & v];
        return new String(ret);
    }
}
