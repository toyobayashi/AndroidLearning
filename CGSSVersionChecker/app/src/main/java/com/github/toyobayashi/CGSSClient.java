package com.github.toyobayashi;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CGSSClient {
    private String user;
    private String viewer;
    private String udid;
    private String sid;
    private String resVer;

    public interface ArgInterface {
        String timezone = "09:00:00";
        String viewer_id = "";
    }

    public interface CheckArgInterface extends ArgInterface {
        String campaign_data = "";
        int campaign_user = 1337;
        String campaign_sign = md5("All your APIs are belong to us");
        int app_type = 0;
    }

    private static String crypto(String str, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(str.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String md5(String str) {
        return crypto(str, "MD5");
    }

    private static String sha1(String str) {
        return crypto(str, "SHA1");
    }

    public CGSSClient(String account, String resVer) {
        String[] accountArr = "-".split(account);
        this.user = accountArr[0];
        this.viewer = accountArr[1];
        this.udid = accountArr[2];
        this.sid = "";
        this.resVer = resVer;
    }

    public CGSSClient(String account) {
        String[] accountArr = "-".split(account);
        this.user = accountArr[0];
        this.viewer = accountArr[1];
        this.udid = accountArr[2];
        this.sid = "";
        this.resVer = "10040400";
    }

    public void post(String path, ArgInterface args) {
        // TODO 发送post请求
    }
}


