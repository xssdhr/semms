package com.xssdhr.service;

import com.xssdhr.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xssdhr
* @description 针对表【sys_user】的数据库操作Service
* @createDate 2023-02-28 16:17:55
*/
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    String getUserAuthorityInfo(Long userId);

    SysUser getByPhoneNum(String phoneNumber);

    SysUser createDefaultUser(String phoneNumber);
}
