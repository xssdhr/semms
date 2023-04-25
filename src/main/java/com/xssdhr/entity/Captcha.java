package com.xssdhr.entity;

/**
 * @author xssdhr
 * @12/4/2023 下午9:35
 * @Company 南京点子跳跃传媒有限公司
 */
public class Captcha {
    private String phonenumber;
    private String captcha;

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public Captcha(String phonenumber, String captcha) {
        this.phonenumber = phonenumber;
        this.captcha = captcha;
    }
}
