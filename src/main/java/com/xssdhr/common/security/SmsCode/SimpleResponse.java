package com.xssdhr.common.security.SmsCode;

/**
 * @author xssdhr
 * @12/4/2023 下午10:31
 * @Company 南京点子跳跃传媒有限公司
 */
public class SimpleResponse {

    private Object content;

    public SimpleResponse(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
