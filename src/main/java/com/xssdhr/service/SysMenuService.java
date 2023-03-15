package com.xssdhr.service;

import com.xssdhr.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author xssdhr
* @description 针对表【sys_menu】的数据库操作Service
* @createDate 2023-03-06 20:29:36
*/
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> buildTreeMenu(List<SysMenu> sysMenuList);
}
