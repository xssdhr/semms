package com.xssdhr.common.security;

import com.xssdhr.entity.CheckResult;
import com.xssdhr.entity.SysUser;
import com.xssdhr.service.SysUserService;
import com.xssdhr.util.JwtUtils;
import com.xssdhr.util.StringUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import com.xssdhr.common.constant.JwtConstant;

/**
 * @author xssdhr
 * @5/3/2023 下午9:26
 * @Company 南京点子跳跃传媒有限公司
 */

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {


    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsService;

    private static final String URL_WHITELIST[] ={
            "/login",
            "/logout",
            "/captcha",
            "/password",
            "/image/**"
    } ;



    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        System.out.println("请求url："+request.getRequestURI());
        //如果token是空，或者url在白名单里，则放行
        if(StringUtil.isEmpty(token) || new ArrayList<String>(Arrays.asList(URL_WHITELIST)).contains(request.getRequestURI())){
            chain.doFilter(request,response);
        }
        //如果token不为空
        CheckResult checkResult = JwtUtils.validateJWT(token);
        if (!checkResult.isSuccess()){
            switch (checkResult.getErrCode()){
                case JwtConstant.JWT_ERRCODE_NULL:throw new JwtException("token不存在");
                case JwtConstant.JWT_ERRCODE_FAIL:throw new JwtException("token验证不通过");
                case JwtConstant.JWT_ERRCODE_EXPIRE:throw new JwtException("token已过期");

            }
        }
        Claims claims = JwtUtils.parseJWT(token);
        String username = claims.getSubject();
        SysUser sysUser = sysUserService.getByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,null,myUserDetailsService.getUserAuthority(sysUser.getId()));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request,response);
    }
}
