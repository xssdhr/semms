package com.xssdhr.common.security;

import cn.hutool.json.JSONUtil;
import com.xssdhr.entity.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt认证失败处理
 * @author xssdhr
 * @6/3/2023 上午12:31
 * @Company 南京点子跳跃传媒有限公司
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse, AuthenticationException e) throws
            IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(JSONUtil.toJsonStr(R.error(HttpServletResponse.SC_UNAUTHORIZED,"认证失败，请登录！")).getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
