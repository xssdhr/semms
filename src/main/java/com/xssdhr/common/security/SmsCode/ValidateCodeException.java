package com.xssdhr.common.security.SmsCode;

import org.springframework.security.core.AuthenticationException;

/**
 * @author xssdhr
 * @12/4/2023 下午10:10
 * @Company 南京点子跳跃传媒有限公司
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
