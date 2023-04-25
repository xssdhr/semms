package com.xssdhr.util;

/**
 * @author xssdhr
 * @10/4/2023 下午5:26
 * @Company 南京点子跳跃传媒有限公司
 * 生成digit位随机数验证码
 */
public class ValidateCodeUtils {
    public static String generateValidateCode(int digit){
        String random = ""+Math.random();
        return random.substring(random.length()-digit);
    }
}
