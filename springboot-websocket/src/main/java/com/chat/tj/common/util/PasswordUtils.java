package com.chat.tj.common.util;

import java.util.Random;

/**
 * 密码工具类
 *
 * @author yinchao@pystandard.com
 * @date 2018/5/25 18:04
 */
public class PasswordUtils {
    /**
     * 获得密码
     *
     * @param len 密码长度
     * @return 随机密码
     */
    public static String createPassWord(int len) {
        Random rd = new Random();
        final int maxNum = 54;
        // 排除歧义输入的字母和数字
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
                'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
                'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5',
                '6', '7', '8', '9'};
        while (true) {
            int count = 0;
            // 取得随机数
            int rdGet;
            // 小写字母标记
            boolean lowercase = false;
            // 大写字母标记
            boolean capitalFlag = false;
            // 数字标记
            boolean numFlag = false;
            // 密码
            StringBuilder sb = new StringBuilder();
            while (count < len) {
                // 生成的数最大为54-1
                rdGet = Math.abs(rd.nextInt(maxNum));
                if (!lowercase && rdGet < 23) {
                    lowercase = true;
                } else if (!capitalFlag && (rdGet >= 23 && rdGet < 46)) {
                    capitalFlag = true;
                } else if (!numFlag && rdGet >= 46) {
                    numFlag = true;
                }
                if (rdGet >= 0 && rdGet < str.length) {
                    sb.append(str[rdGet]);
                    count++;
                }
            }
            if (lowercase && capitalFlag && numFlag) {
                return sb.toString();
            }
        }
    }
}
