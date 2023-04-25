package com.xssdhr.controller;

import com.xssdhr.entity.Captcha;
import com.xssdhr.entity.R;
import com.xssdhr.common.security.SmsCode.SendSms;
import com.xssdhr.util.StringUtil;
import com.xssdhr.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 手机验证码
 * @author xssdhr
 * @10/4/2023 下午8:51
 * @Company 南京点子跳跃传媒有限公司
 */
@CrossOrigin
@RestController
@RequestMapping("/captcha")
public class CaptchaController {


    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    private static final Logger log = Logger.getLogger(SysUserController.class.getName());// <= (2)

    @CrossOrigin
    @PostMapping("/sendMsg")
    public R sendMsg(@RequestBody Captcha captcha) throws Exception {
        String phonenumber = captcha.getPhonenumber();
        if(StringUtil.isNotEmpty(phonenumber)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云的api接口发送验证码
            if(SendSms.sendSmsWithCode(code,phonenumber)){
                // 优化，使用Redis缓存验证码，5分钟失效
                ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
                ops.set(phonenumber,code,10, TimeUnit.MINUTES);
                System.out.println("验证码--->({"+phonenumber+"} --- {"+code+"})");
                return R.ok();
            }else{
                return R.error("验证码发送失败");
            }
        }
        return R.error("验证码发送失败");
    }

}
