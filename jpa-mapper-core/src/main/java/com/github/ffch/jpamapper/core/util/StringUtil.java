package com.github.ffch.jpamapper.core.util;

/**
 * 字符串工具
 *
 * @author fufei
 */
public class StringUtil {
    public static Boolean isEmpty(String data) {
        return data == null || "".equals(data.trim());
    }

    public static Boolean isEmpty(Object data) {
        return data == null || "".equals(data.toString().trim());
    }

    public static Boolean isNotEmpty(String data) {
        return !isEmpty(data);
    }

    public static Boolean isNotEmpty(Object data) {
        return !isEmpty(data);
    }

    public static String format(String value, int length) {
        if (value.length() > length) {
            value = value.substring(0, length);
        }
        return String.format("%-" + length + "s", value);
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    /**
     * String左对齐,向其右端填充指定字符${ch}至${len}长度
     *
     * @param src 目标字符串
     * @param ch  需要填充的字符
     * @param len 填充后的长度
     * @return 操作后的字符串
     */
    public static String padLeft(String src, char ch, int len) {
        int srcLength = src.length();
        int diff = len - srcLength;
        if (diff <= 0) {
            return src;
        }
        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, srcLength);
        for (int i = srcLength; i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    /**
     * String右对齐,向其左端填充指定字符${ch}至${len}长度
     *
     * @param src 目标字符串
     * @param ch  需要填充的字符
     * @param len 填充后的长度
     * @return 操作后的字符串
     */
    public static String padRight(String src, char ch, int len) {
        int srcLength = src.length();
        int diff = len - srcLength;
        if (diff <= 0) {
            return src;
        }
        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, srcLength);
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

}
