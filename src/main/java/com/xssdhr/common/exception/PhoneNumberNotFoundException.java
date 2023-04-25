package com.xssdhr.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author xssdhr
 * @13/4/2023 下午8:42
 * @Company 南京点子跳跃传媒有限公司
 */
public class PhoneNumberNotFoundException extends AuthenticationException {
    public PhoneNumberNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PhoneNumberNotFoundException(String msg) {
        super(msg);
    }
}
