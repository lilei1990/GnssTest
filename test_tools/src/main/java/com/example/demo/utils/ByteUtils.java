package com.example.demo.utils;

import java.io.Closeable;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ByteUtils {

    private static final String HEX = "0123456789ABCDEF";

    public static byte charToByte(char c) {
        return (byte) HEX.indexOf(c);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (ByteUtils.charToByte(hexChars[pos]) << 4 | ByteUtils.charToByte(
                hexChars[pos + 1]));
        }
        return d;
    }

    /*将bytes数组转化为16进制字符串*/
    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
    /*将bytes数组转化为16进制字符串*/
    public static String bytesToHexStringDebug(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase()+" ");
        }
        return sb.toString();
    }

    public static long bytesToLong(byte[] b) {
        if (b.length != 4) {
            return -1;
        }
        return bytesToLong(b[0], b[1], b[2], b[3]);
    }

    public static long bytesToLong(byte b1, byte b2, byte b3, byte b4) {
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8) | ((b3 & 0xFF) << 8 * 2) | ((b4 & 0xFF) << 8 * 3);
    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[4];
        result[0] = (byte) (l & 0xFF);
        result[1] = (byte) (l >> 8 & 0xFF);
        result[2] = (byte) (l >> 16 & 0xFF);
        result[3] = (byte) (l >> 24 & 0xFF);
        return result;
    }

    public static int bytesToInt(byte b1, byte b2) {
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8);
    }

    public static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    // char[]转byte[]
    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);

        return bb.array();
    }

    public static byte getByte(char c) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(1);
        cb.put(c);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);

        byte[] tmp = bb.array();
        return tmp[0];
    }

    // byte转char
    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        return cb.array();
    }

    // byte转char
    public static char getChar(byte bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(1);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        char[] tmp = cb.array();

        return tmp[0];
    }

    /**
     * 字符串转字节
     *
     * @param s 字符串
     * @return 字节
     */
    public static byte[] stringToBytes(String s) {
        return stringToBytes(s, 0);
    }

    /**
     * 字符串转字节
     *
     * @param s 字符串
     * @param length 字节长度，不够补0
     */
    public static byte[] stringToBytes(String s, int length) {
        byte[] src =
            ByteUtils.getBytes((s == null || s.length() == 0) ? new char[0] : s.toCharArray());

        if (length < src.length) {
            length = src.length;
        }

        byte[] bytes = new byte[length];
        for (int i = 0; i < src.length; i++) {
            bytes[i] = src[i];
        }
        return bytes;
    }

    public static byte[] copy(byte[] src, int srcStart, int length) {
        byte[] result = new byte[length];
        System.arraycopy(src, srcStart, result, 0, length);
        return result;
    }

    public static byte[] reverse(byte[] src) {
        for (int i = 0; i <= src.length / 2 - 1; i++) {
            byte temp1 = src[i];
            byte temp2 = src[src.length - i - 1];
            src[i] = temp2;
            src[src.length - i - 1] = temp1;
        }
        return src;
    }

    public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
        if ((bytes == null) || (bytes.length == 0) || (offset < 0) || (dateLen <= 0)) {
            return null;
        }
        if ((offset >= bytes.length) || (bytes.length - offset < dateLen)) {
            return null;
        }

        String asciiStr = null;
        byte[] data = new byte[dateLen];
        System.arraycopy(bytes, offset, data, 0, dateLen);
        try {
            asciiStr = new String(data, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
        }
        return asciiStr;
    }

    public static String bytesToAscii(byte[] bytes, int dateLen) {
        return bytesToAscii(bytes, 0, dateLen);
    }

    public static String bytesToAscii(byte[] bytes) {
        return bytesToAscii(bytes, 0, bytes.length);
    }

    /**
     * 字符串 转 ascii bytes
     * ISO-8859-1 码表兼容大部分码表，因此适合做中间码表
     */
    public static byte[] stringToAsciiBytes(String s) {
        try {
            return s.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = new byte[first.length + second.length];
        //byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);

        return result;

    }

    /**
     * 取一个字节的高几位bit
     * @param b
     * @param length
     * @return
     */
    public static int getLeftNum(byte b,int length) {
        return b>>(8-length);
    }
    /**
     * 取一个字节的低几位bit
     * @param b
     * @param length
     * @return
     */
    public static int getRightNum(byte b,int length) {
        byte mv=(byte) (0xff>>(8-length));
        return b&mv;
    }
    /**
     * 取中间几位，包括 startIndex位和endIndex位
     * @param b
     * @param startIndex
     * @param endIndex
     * @return
     */
    private static int getMidNum(byte b,int startIndex,int endIndex) {
        byte i=(byte) getLeftNum(b,endIndex+1);//先取高几位
        return getRightNum(i,endIndex-startIndex+1);//再取低几位
    }

    /**
     * 从低位到高位
     * @param b
     * @param lower
     * @param height
     * @return
     */
    public static int getLowerToHeight(byte b,int lower,int height) {
        //5,7  -->0,2

        return getMidNum(b, 8 - height - 1, 8 - lower - 1);

       /* byte i=(byte) getRightNum(b,8-lower+1);//先取高几位
        return getLeftNum(i,height-lower+1);//再取低几位*/
    }

    /**
     * 表示版本文件的总长度（小端格式）
     * @param strBytes
     * @return
     */
    public static byte[] getUpdateLenReverse(byte[] strBytes) {
        String s = Long.toHexString(strBytes.length);

        System.out.println("getUpdateLenReverse:"+ s);
        System.out.println("getUpdateLenReverse:"+ addZeroForNum(s,8));

        byte[] reverse = reverse(hexStringToBytes(addZeroForNum(s, 8)));
        System.out.println("getUpdateLenReverse reverse:"+ bytesToHexString(reverse));
        byte[] rst = new byte[8];
        rst[0] = (byte) 0x80;
        rst[1] = (byte) 0x01;
        rst[2] = (byte) 0x00;
        rst[3] = (byte) 0x00;
        System.arraycopy(reverse,0,rst,4,4);
        return rst;
    }

    private static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }
    /**
     * 表示版本文件整个文件的CRC32（小端格式）
     * @param strBytes
     * @return
     */
    public static byte[] getCRC32Reverse(byte[] strBytes) {
        /*java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
        crc32.update(strBytes);
        String s = Long.toHexString(crc32.getValue());*/

        //System.out.println("CRC32Reverse:"+ bytesToHexString(hexStringToBytes(s)));
        //return reverse(hexStringToBytes(s));

        String crc32S = getCRC32(ByteUtils.bytesToHexString(strBytes));
        System.out.println("CRC32Reverse:"+crc32S);
        byte[] reverse = reverse(hexStringToBytes(addZeroForNum(crc32S,8)));
        byte[] rst = new byte[8];
        rst[0] = (byte) 0x80;
        rst[1] = (byte) 0x02;
        rst[2] = (byte) 0x00;
        rst[3] = (byte) 0x00;
        System.arraycopy(reverse,0,rst,4,reverse.length);
        return rst;
    }
    /**
     * 表示版本文件整个文件的CRC32（小端格式）
     * @param strBytes
     * @return
     */
    public static byte[] getCRC32Reverse(byte[] strBytes,boolean isEndSegment) {
        /*java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
        crc32.update(strBytes);
        String s = Long.toHexString(crc32.getValue());*/

        //System.out.println("CRC32Reverse:"+ bytesToHexString(hexStringToBytes(s)));
        //return reverse(hexStringToBytes(s));

        String crc32S = getCRC32(ByteUtils.bytesToHexString(strBytes));
        System.out.println("CRC32Reverse:"+crc32S);
        byte[] reverse = reverse(hexStringToBytes(addZeroForNum(crc32S,8)));
        byte[] rst = new byte[8];
        rst[0] = (byte) 0x80;
        rst[1] = (byte) 0x02;
        if (isEndSegment) {

            rst[2] = (byte) 0x01;
        } else {
            rst[2] = (byte) 0x00;
        }
        rst[3] = (byte) 0x00;
        System.arraycopy(reverse,0,rst,4,reverse.length);
        return rst;
    }
    private static String getCRC32(String str){
        int[] table = {
                0x00000000, 0x77073096, 0xee0e612c, 0x990951ba, 0x076dc419, 0x706af48f, 0xe963a535, 0x9e6495a3,
                0x0edb8832, 0x79dcb8a4, 0xe0d5e91e, 0x97d2d988, 0x09b64c2b, 0x7eb17cbd, 0xe7b82d07, 0x90bf1d91,
                0x1db71064, 0x6ab020f2, 0xf3b97148, 0x84be41de, 0x1adad47d, 0x6ddde4eb, 0xf4d4b551, 0x83d385c7,
                0x136c9856, 0x646ba8c0, 0xfd62f97a, 0x8a65c9ec, 0x14015c4f, 0x63066cd9, 0xfa0f3d63, 0x8d080df5,
                0x3b6e20c8, 0x4c69105e, 0xd56041e4, 0xa2677172, 0x3c03e4d1, 0x4b04d447, 0xd20d85fd, 0xa50ab56b,
                0x35b5a8fa, 0x42b2986c, 0xdbbbc9d6, 0xacbcf940, 0x32d86ce3, 0x45df5c75, 0xdcd60dcf, 0xabd13d59,
                0x26d930ac, 0x51de003a, 0xc8d75180, 0xbfd06116, 0x21b4f4b5, 0x56b3c423, 0xcfba9599, 0xb8bda50f,
                0x2802b89e, 0x5f058808, 0xc60cd9b2, 0xb10be924, 0x2f6f7c87, 0x58684c11, 0xc1611dab, 0xb6662d3d,
                0x76dc4190, 0x01db7106, 0x98d220bc, 0xefd5102a, 0x71b18589, 0x06b6b51f, 0x9fbfe4a5, 0xe8b8d433,
                0x7807c9a2, 0x0f00f934, 0x9609a88e, 0xe10e9818, 0x7f6a0dbb, 0x086d3d2d, 0x91646c97, 0xe6635c01,
                0x6b6b51f4, 0x1c6c6162, 0x856530d8, 0xf262004e, 0x6c0695ed, 0x1b01a57b, 0x8208f4c1, 0xf50fc457,
                0x65b0d9c6, 0x12b7e950, 0x8bbeb8ea, 0xfcb9887c, 0x62dd1ddf, 0x15da2d49, 0x8cd37cf3, 0xfbd44c65,
                0x4db26158, 0x3ab551ce, 0xa3bc0074, 0xd4bb30e2, 0x4adfa541, 0x3dd895d7, 0xa4d1c46d, 0xd3d6f4fb,
                0x4369e96a, 0x346ed9fc, 0xad678846, 0xda60b8d0, 0x44042d73, 0x33031de5, 0xaa0a4c5f, 0xdd0d7cc9,
                0x5005713c, 0x270241aa, 0xbe0b1010, 0xc90c2086, 0x5768b525, 0x206f85b3, 0xb966d409, 0xce61e49f,
                0x5edef90e, 0x29d9c998, 0xb0d09822, 0xc7d7a8b4, 0x59b33d17, 0x2eb40d81, 0xb7bd5c3b, 0xc0ba6cad,
                0xedb88320, 0x9abfb3b6, 0x03b6e20c, 0x74b1d29a, 0xead54739, 0x9dd277af, 0x04db2615, 0x73dc1683,
                0xe3630b12, 0x94643b84, 0x0d6d6a3e, 0x7a6a5aa8, 0xe40ecf0b, 0x9309ff9d, 0x0a00ae27, 0x7d079eb1,
                0xf00f9344, 0x8708a3d2, 0x1e01f268, 0x6906c2fe, 0xf762575d, 0x806567cb, 0x196c3671, 0x6e6b06e7,
                0xfed41b76, 0x89d32be0, 0x10da7a5a, 0x67dd4acc, 0xf9b9df6f, 0x8ebeeff9, 0x17b7be43, 0x60b08ed5,
                0xd6d6a3e8, 0xa1d1937e, 0x38d8c2c4, 0x4fdff252, 0xd1bb67f1, 0xa6bc5767, 0x3fb506dd, 0x48b2364b,
                0xd80d2bda, 0xaf0a1b4c, 0x36034af6, 0x41047a60, 0xdf60efc3, 0xa867df55, 0x316e8eef, 0x4669be79,
                0xcb61b38c, 0xbc66831a, 0x256fd2a0, 0x5268e236, 0xcc0c7795, 0xbb0b4703, 0x220216b9, 0x5505262f,
                0xc5ba3bbe, 0xb2bd0b28, 0x2bb45a92, 0x5cb36a04, 0xc2d7ffa7, 0xb5d0cf31, 0x2cd99e8b, 0x5bdeae1d,
                0x9b64c2b0, 0xec63f226, 0x756aa39c, 0x026d930a, 0x9c0906a9, 0xeb0e363f, 0x72076785, 0x05005713,
                0x95bf4a82, 0xe2b87a14, 0x7bb12bae, 0x0cb61b38, 0x92d28e9b, 0xe5d5be0d, 0x7cdcefb7, 0x0bdbdf21,
                0x86d3d2d4, 0xf1d4e242, 0x68ddb3f8, 0x1fda836e, 0x81be16cd, 0xf6b9265b, 0x6fb077e1, 0x18b74777,
                0x88085ae6, 0xff0f6a70, 0x66063bca, 0x11010b5c, 0x8f659eff, 0xf862ae69, 0x616bffd3, 0x166ccf45,
                0xa00ae278, 0xd70dd2ee, 0x4e048354, 0x3903b3c2, 0xa7672661, 0xd06016f7, 0x4969474d, 0x3e6e77db,
                0xaed16a4a, 0xd9d65adc, 0x40df0b66, 0x37d83bf0, 0xa9bcae53, 0xdebb9ec5, 0x47b2cf7f, 0x30b5ffe9,
                0xbdbdf21c, 0xcabac28a, 0x53b39330, 0x24b4a3a6, 0xbad03605, 0xcdd70693, 0x54de5729, 0x23d967bf,
                0xb3667a2e, 0xc4614ab8, 0x5d681b02, 0x2a6f2b94, 0xb40bbe37, 0xc30c8ea1, 0x5a05df1b, 0x2d02ef8d,
        };
        byte[] bytes = hexStringToBytes(str);
//        int crc = 0xffffffff;//0
        int crc = 0;//0
        for (byte b : bytes) {
            crc = (crc >>>8 ^ table[(crc ^ b) & 0xff]);
        }
//        crc = crc ^ 0xffffffff;//0
        crc = crc ^ 0;//0
        // System.out.println(crc);


        return Integer.toHexString(crc);
    }
    public static List<byte[]> getUpdate6Bytes(byte[] strBytes) {
        List<byte[]> list = new ArrayList<>();
        int index = 0;
        byte[] tmp = new byte[0];
        System.out.println("--->strBytes.length="+strBytes.length);
        while (index < strBytes.length) {

            tmp = ByteUtils.concat(tmp, new byte[]{strBytes[index]});

            if (tmp.length== 6) {
                tmp = ByteUtils.concat(new byte[]{(byte) 0x81, (byte) (list.size() % 256)}, tmp);
                list.add(tmp);
                tmp = new byte[0];
                index++;
                continue;


            }
            if ((index + 1) % 4096 == 0) {
                //System.out.println("--->index="+index);
                tmp = ByteUtils.concat(new byte[]{(byte) 0x81, (byte) (list.size() % 256)}, tmp);
                list.add(tmp);
                tmp = new byte[0];

                index++;
                continue;
            }

            if (index == strBytes.length - 1 && tmp.length > 0) {
                tmp = ByteUtils.concat(new byte[]{(byte) 0x81, (byte) (list.size() % 256)}, tmp);
                list.add(tmp);
            }

            index++;

        }

        /*for (int i = 0; i < strBytes.length; i+=6) {
            byte[] tmp = new byte[8];

            int len = i + 6;
            if (i + 6 > strBytes.length) {
                len = strBytes.length - i;
            } else {
                len=6;
            }
            tmp[1] = (byte) (list.size() % 256);
            tmp[0] = (byte)0x81;
            System.arraycopy(strBytes,i,tmp,2,len);
            list.add(tmp);
        }*/
        /*for (byte[] bytes : list) {
            System.out.println(bytesToHexString(bytes));
        }*/
        //System.out.println(bytesToHexString(strBytes));
        /*for (int i = 0; i < list.size(); i++) {
            System.out.println("序号 "+i+" 帧序号 --> "+(i%256)+" --> "+bytesToHexString(list.get(i)));
        }*/



        return list;
    }
    public static int byteToInt(byte b){
        int x = b & 0xff;
        return x;
    }

    /**
     * 无符号，int 占 2 个字节
     */
    public static int convertTwoUnsignInt(byte b1, byte b2)      // unsigned
    {
        return (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }
    public static byte[] longToByte3(long n) {


        byte[] b = new byte[3];
        b[2] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[0] = (byte) (n >> 16 & 0xff);


        return b;
    }

    //byte[] 转 int 高字节在前（高字节序）
    public static int tobyte3ToLong(byte[] b){
        int res = 0;
        for(int i=0;i<b.length;i++){
            res += (b[i] & 0xff) << ((2-i)*8);
        }
        return res;
    }
    public static byte[] readFile(File file) {
        RandomAccessFile rf = null;
        byte[] data = null;
        try {
            rf = new RandomAccessFile(file, "r");
            data = new byte[(int) rf.length()];
            rf.readFully(data);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            closeQuietly(rf);
        }
        return data;
    }
    //关闭读取file
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(HexUtil.toHexString(longToByte3(i))+"  "+tobyte3ToLong(longToByte3(i)));
        }
    }
}
