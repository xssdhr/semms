package com.xssdhr.common.security;

import cn.hutool.json.JSONUtil;
import com.xssdhr.entity.R;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义logout处理
 * @author xssdhr
 * @6/3/2023 上午12:57
 * @Company 南京点子跳跃传媒有限公司
 */
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(JSONUtil.toJsonStr(R.ok("登出成功")).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
