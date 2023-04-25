package com.xssdhr.common.security.SmsCode;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author xssdhr
 * @12/4/2023 下午10:17
 * @Company 南京点子跳跃传媒有限公司
 */
@Component
public class SmsCodeGenerator{
    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(4);
        return new ValidateCode(code, 60);
    }
}
