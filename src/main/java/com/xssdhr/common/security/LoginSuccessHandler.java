package com.xssdhr.common.security;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xssdhr.entity.R;
import com.xssdhr.entity.SysMenu;
import com.xssdhr.entity.SysRole;
import com.xssdhr.entity.SysUser;
import com.xssdhr.service.SysMenuService;
import com.xssdhr.service.SysRoleService;
import com.xssdhr.service.SysUserService;
import com.xssdhr.util.JwtUtils;
import com.xssdhr.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 登录成功处理器
 * @author xssdhr
 * @3/3/2023 下午7:52
 * @Company 南京点子跳跃传媒有限公司
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        String username=authentication.getName();
        String token = JwtUtils.genJwtToken(username);

        SysUser currentUser = sysUserService.getByUsername(username);

        //根据用户id获取所有的角色信息
        List<SysRole> roleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id","SELECT role_id FROM sys_user_role WHERE user_id="+currentUser.getId()));

        //遍历所有的角色，获取所有菜单权限，而且不重复
        Set<SysMenu> menuSet = new HashSet<>();
        for(SysRole sysRole:roleList){
            List<SysMenu> sysMenuList = sysMenuService.list(new QueryWrapper<SysMenu>().inSql("id", "SELECT menu_id FROM sys_role_menu WHERE role_id=" + sysRole.getId()));
            for(SysMenu sysMenu:sysMenuList){
                menuSet.add(sysMenu);
            }
        }

        currentUser.setRoles(roleList.stream().map(SysRole::getName).collect(Collectors.joining(",")));
        List<SysMenu> sysMenuList = new ArrayList<>(menuSet);
        //排序
        sysMenuList.sort(Comparator.comparing(SysMenu::getOrderNum));


        //转换为菜单树
        List<SysMenu> menuList = sysMenuService.buildTreeMenu(sysMenuList);


        outputStream.write(JSONUtil.toJsonStr(R.ok("登录成功").put("authorization",token).put("currentUser",currentUser).put("menuList",menuList)).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
