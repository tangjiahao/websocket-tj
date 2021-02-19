package com.chat.tj.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.apache.commons.codec.CharEncoding.UTF_8;

public class PyEncryption {

    // 设定的编码模式为SHA-256
    private static final String ENCRYPT_MODE = "SHA-256";
    // 设定随机源
    private static final String RANDOM_SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*_+:";

    // length表示生成字符串的长度
    private synchronized static String getRandomString() {
        /**
         * 功能描述：产生随机字符串
         */
        Random rd = new Random();
        int length = rd.nextInt(96) + rd.nextInt(32);
        System.out.println(length);
        String base = RANDOM_SOURCE;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public synchronized static EncryptModel encrypt_passwd_SHA(String org_str) throws Exception {

        /**
         * 用于产生字符串的加密结果，并将两个盐值（特征值）返回
         */

        // 加密后的字符串
        String user_pwd = "";
        // 随机盐值1
        String user_pwd_flag1 = getRandomString();
        // 随机颜值2
        String usr_pwd_flag2 = getRandomString();
        // 对要加密的字段进行加盐
        String org_str_add = user_pwd_flag1 + org_str + usr_pwd_flag2;

        // System.out.println(org_str);
        byte[] data = org_str_add.getBytes();

        MessageDigest md = MessageDigest.getInstance(ENCRYPT_MODE);
        // md.update(data);
        byte[] digest = md.digest(data);
        // System.out.println("digest:"+digest);
        user_pwd = new HexBinaryAdapter().marshal(digest);

        return new EncryptModel(org_str, user_pwd_flag1, usr_pwd_flag2, user_pwd);
    }

    public synchronized static Boolean validate_encry(String org_str, String user_pwd_flag1, String usr_pwd_flag2,
                                                      String user_pwd) throws NoSuchAlgorithmException {
        Boolean flag = false;
        if (org_str != null && !"".equals(org_str) && user_pwd_flag1 != null && !"".equals(user_pwd_flag1)
                && usr_pwd_flag2 != null && !"".equals(usr_pwd_flag2) && user_pwd != null && !"".equals(user_pwd)) {
            String encryptSHA_str = "";
            // 对要加密的字段进行加盐
            org_str = user_pwd_flag1 + org_str + usr_pwd_flag2;
            byte[] data = org_str.getBytes();
            MessageDigest md = MessageDigest.getInstance(ENCRYPT_MODE);
            // md.update(data);
            byte[] digest = md.digest(data);
            // System.out.println("digest:"+digest);
            encryptSHA_str = new HexBinaryAdapter().marshal(digest);
            if (user_pwd.equals(encryptSHA_str)) {
                // 如果加密结果一致，则为真
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 生成token
     *
     * @param user_pwd_input
     * @return
     */
    public static String createToken(String user_pwd_input) {
        String salt = System.nanoTime() + getRandomString();
        user_pwd_input = user_pwd_input == null ? salt : user_pwd_input + salt;
        byte[] data = user_pwd_input.getBytes();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(ENCRYPT_MODE);
            byte[] digest = md.digest(data);
            return new HexBinaryAdapter().marshal(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 根据盐生成加密后的密码
     *
     * @param org_str
     * @param user_pwd_flag1
     * @param usr_pwd_flag2
     * @return
     * @throws Exception
     */
    public synchronized static String encrypt_passwd_SHA_bysalt(String org_str, String user_pwd_flag1,
                                                                String usr_pwd_flag2) throws Exception {
        /**
         * 用于产生字符串的加密结果，并将两个盐值（特征值）返回
         */
        // 加密后的字符串
        String user_pwd = "";
        // 对要加密的字段进行加盐
        org_str = user_pwd_flag1 + org_str + usr_pwd_flag2;
        // System.out.println(org_str);
        byte[] data = org_str.getBytes();
        MessageDigest md = MessageDigest.getInstance(ENCRYPT_MODE);
        // md.update(data);
        byte[] digest = md.digest(data);
        // System.out.println("digest:"+digest);
        user_pwd = new HexBinaryAdapter().marshal(digest);
        return user_pwd;
    }

    /**
     * 对给定的字符串进行base64解码操作
     */
    public static String decodeData(String inputData) {
        try {
            if (null == inputData) {
                return null;
            }
            return new String(Base64.decodeBase64(inputData.getBytes(UTF_8)), UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class EncryptModel {
        private String org_str;// 密码明文
        private String user_pwd_flag1;// 盐1
        private String usr_pwd_flag2;// 盐2
        private String user_pwd;// 加密的密码

        public EncryptModel() {
        }

        public EncryptModel(String org_str, String user_pwd_flag1, String usr_pwd_flag2, String user_pwd) {
            super();
            this.org_str = org_str;
            this.user_pwd_flag1 = user_pwd_flag1;
            this.usr_pwd_flag2 = usr_pwd_flag2;
            this.user_pwd = user_pwd;
        }

        public String getOrg_str() {
            return org_str;
        }

        public void setOrg_str(String org_str) {
            this.org_str = org_str;
        }

        public String getUser_pwd_flag1() {
            return user_pwd_flag1;
        }

        public void setUser_pwd_flag1(String user_pwd_flag1) {
            this.user_pwd_flag1 = user_pwd_flag1;
        }

        public String getUsr_pwd_flag2() {
            return usr_pwd_flag2;
        }

        public void setUsr_pwd_flag2(String usr_pwd_flag2) {
            this.usr_pwd_flag2 = usr_pwd_flag2;
        }

        public String getUser_pwd() {
            return user_pwd;
        }

        public void setUser_pwd(String user_pwd) {
            this.user_pwd = user_pwd;
        }

    }

}
