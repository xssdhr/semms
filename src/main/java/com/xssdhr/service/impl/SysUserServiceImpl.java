package com.xssdhr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xssdhr.entity.SysMenu;
import com.xssdhr.entity.SysRole;
import com.xssdhr.entity.SysUser;
import com.xssdhr.mapper.SysMenuMapper;
import com.xssdhr.mapper.SysRoleMapper;
import com.xssdhr.service.SysUserService;
import com.xssdhr.mapper.SysUserMapper;
import com.xssdhr.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author xssdhr
* @description 针对表【sys_user】的数据库操作Service实现
* @createDate 2023-02-28 16:17:55
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username",username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        StringBuffer authority = new StringBuffer();
        //根据用户id获取所有的角色信息
        List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>().inSql("id","SELECT role_id FROM sys_user_role WHERE user_id="+userId));
        if(roleList.size()>0){
            String roleCodeStr = roleList.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
            authority.append(roleCodeStr);
        }
        //遍历所有的角色，获取所有菜单权限，而且不重复
        Set<String> menuCodeSet = new HashSet<>();
        for(SysRole sysRole:roleList){
            List<SysMenu> sysMenuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().inSql("id", "SELECT menu_id FROM sys_role_menu WHERE role_id=" + sysRole.getId()));
            for(SysMenu sysMenu:sysMenuList){
                String perms = sysMenu.getPerms();
                if(StringUtil.isNotEmpty(perms)){
                    menuCodeSet.add(perms);
                }
            }
        }
        if(menuCodeSet.size()>0){
            authority.append(",");
            String menuCodeStr = menuCodeSet.stream().collect(Collectors.joining(","));
            authority.append(menuCodeStr);
        }

        System.out.println("authority:"+authority.toString());
        return authority.toString();
    }
}




