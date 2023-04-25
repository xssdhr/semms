package com.xssdhr.common.security;

import com.xssdhr.common.exception.PhoneNumberNotFoundException;
import com.xssdhr.common.exception.UserCountLockException;
import com.xssdhr.entity.SysUser;
import com.xssdhr.entity.SysUserRole;
import com.xssdhr.service.SysUserRoleService;
import com.xssdhr.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 自定义UserDetail
 * @author xssdhr
 * @4/3/2023 下午10:17
 * @Company 南京点子跳跃传媒有限公司
 */
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${default.defaultPassWord}")
    private String defaultPassword;

    @Value("${default.defaultavatarMomoPath}")
    private String defaultAvatarUrl;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if(sysUser == null){
            throw new UsernameNotFoundException("用户名或密码错误！");
        }else if("1".equals(sysUser.getStatus())){
            throw new UserCountLockException("该用户账号被封禁，具体请联系管理员！");
        }

        return new User(sysUser.getUsername(),sysUser.getPassword(),getUserAuthority(sysUser.getId()));
    }
    public UserDetails loadUserByPhoneNumber(String phoneNumber){
        SysUser sysUser = sysUserService.getByPhoneNum(phoneNumber);
        if(sysUser == null){
            String newName = "手机用户"+phoneNumber;
            SysUser sysNewUser = new SysUser(newName,bCryptPasswordEncoder.encode(defaultPassword),defaultAvatarUrl,phoneNumber,"0");
            sysNewUser.setCreateTime(new Date());
            sysNewUser.setUpdateTime(new Date());
            sysUserService.save(sysNewUser);
            List<SysUserRole> userRoleList = new ArrayList<>();
            SysUserRole sysUserRole = new SysUserRole();
            //赋初始权限为游客（roleId = 26）
            sysUserRole.setRoleId(26L);
            sysUserRole.setUserId(sysNewUser.getId());
            sysUserRoleService.save(sysUserRole);
            return new User(sysNewUser.getUsername(),sysNewUser.getPassword(),getUserAuthority(sysNewUser.getId()));
        }else{
            return new User(sysUser.getUsername(),sysUser.getPassword(),getUserAuthority(sysUser.getId()));
        }

    }

    public List< GrantedAuthority> getUserAuthority(Long userId) {
// 格式
//        ROLE_admin,ROLE_common,system:user:resetPwd,system:role:delete,system:user:list,system:menu:query,system:menu:list,system:menu:add,system:user:delete,system:rol
//        e:list,system:role:menu,system:user:edit,system:user:query,system:role:edit,system:user:add,system:user:role,system:menu:delete,system:role:add,system:role:query,system:menu:edit
        String authority=sysUserService.getUserAuthorityInfo(userId);
        System.out.println("authority="+authority);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
