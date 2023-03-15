package com.xssdhr.controller;


import com.xssdhr.entity.R;
import com.xssdhr.entity.SysUser;
import com.xssdhr.service.SysUserService;
import com.xssdhr.util.JwtUtils;
import com.xssdhr.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/user/list")
    //判断角色
//    @PreAuthorize("hasRole('ROLE_admin')")//
    @PreAuthorize("hasAuthority('system:user:list')")
    public R userList(@RequestHeader(required = false) String token){
        System.out.println(token);
        if(StringUtil.isNotEmpty(token)){
            Map<String, Object> resultMap = new HashMap<>();
            List<SysUser> userList = sysUserService.list();
            resultMap.put("userList",userList);
            return R.ok(resultMap);

        }else{
            return R.error(401,"没有权限访问");
        }

    }

    @RequestMapping("/login")
    public R login(){
        String token = JwtUtils.genJwtToken("xssdhr0");
        return R.ok().put("token",token);
    }


}
