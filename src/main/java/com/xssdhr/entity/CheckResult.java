package com.xssdhr.entity;

import io.jsonwebtoken.Claims;

/**
 * jwt验证信息
 * @author xssdhr
 * @company 南京点子跳跃传媒有限公司
 * @create 2022-08-13 上午 10:00
 */
public class CheckResult {

    private int errCode;

    private boolean success;

    private Claims claims;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

}
