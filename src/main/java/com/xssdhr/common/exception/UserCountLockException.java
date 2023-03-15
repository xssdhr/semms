package com.xssdhr.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author xssdhr
 * @4/3/2023 下午10:40
 * @Company 南京点子跳跃传媒有限公司
 */
public class UserCountLockException extends AuthenticationException {
    public UserCountLockException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserCountLockException(String msg) {
        super(msg);
    }
}
