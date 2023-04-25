package com.xssdhr.common.security.SmsCode;

import com.xssdhr.common.security.MyUserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author xssdhr
 * @12/4/2023 下午10:46
 * @Company 南京点子跳跃传媒有限公司
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private MyUserDetailsServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsCodeAuthenticationToken smsCodeAuthenticationToken = (SmsCodeAuthenticationToken) authentication;
//        UserDetails user = userDetailsService.loadUserByUsername((String)smsCodeAuthenticationToken.getPrincipal());
        UserDetails user = userDetailsService.loadUserByPhoneNumber((String)smsCodeAuthenticationToken.getPrincipal());
        if(user == null){
            throw new InternalAuthenticationServiceException("用户不存在");
        }

        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user,user.getAuthorities());
        authenticationResult.setDetails(smsCodeAuthenticationToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(MyUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

}