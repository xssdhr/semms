package com.xssdhr.common.security.SmsCode;




import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author xssdhr
 * @12/4/2023 下午10:08
 * @Company 南京点子跳跃传媒有限公司
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {


        if(StringUtils.equals("/captcha/login", httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")){
            try {
                validateSmsCode(httpServletRequest,httpServletRequest.getSession());
            }catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                return;
            }
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    //校验手机验证码
    private void validateSmsCode(HttpServletRequest request, HttpSession session) throws ServletRequestBindingException {
        //请求里的手机号和验证码
        String mobileInRequest = request.getParameter("phonenumber");
        String codeInRequest = request.getParameter("captcha");
        String captchaInRedis = null;
        if(stringRedisTemplate.hasKey(mobileInRequest)){
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            captchaInRedis = ops.get(mobileInRequest).toString();
        }

        if (StringUtils.isEmpty(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if(captchaInRedis == null){
            throw new ValidateCodeException("该手机号未发送验证码或验证码过期");
        }


        if(!StringUtils.equals(captchaInRedis, codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        session.removeAttribute(mobileInRequest);
    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

}
