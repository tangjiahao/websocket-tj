package com.chat.tj.Test;

import com.chat.tj.common.util.PasswordUtils;
import com.chat.tj.common.util.PyEncryption;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author tangjing
 * @date 2021/02/19 10:48
 */
public class TokenTest {
    public static String getSignature() {
        String ak = "JWERNB76KSZM8UWC";
        String sk = "9EPQYRF6sYvGBBVk3abwnzvYcuk5ZYeV";
        String time = "" + System.currentTimeMillis();
        String signature = DigestUtils.md5Hex(ak + time + sk);
        System.out.println(time);
        System.out.println(signature);
        return signature;
    }

    public static String getToken() {
        String token = PyEncryption.createToken(PasswordUtils.createPassWord(8));
        return token;
    }

    public static void main(String[] args) {
        // System.out.println(getSignature());
        // System.out.println(getToken());
        getSignature();
    }
}
