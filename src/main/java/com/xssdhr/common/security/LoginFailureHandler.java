package com.xssdhr.common.security;

import cn.hutool.json.JSONUtil;
import com.xssdhr.entity.R;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 * @author xssdhr
 * @3/3/2023 下午7:52
 * @Company 南京点子跳跃传媒有限公司
 */

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        String message=exception.getMessage();
        if(exception instanceof BadCredentialsException){
            message="用户名或者密码错误！";
        }
        outputStream.write(JSONUtil.toJsonStr(R.error(message)).getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
