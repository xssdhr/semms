package com.xssdhr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xssdhr.common.constant.Constant;
import com.xssdhr.entity.*;
import com.xssdhr.service.SysRoleService;
import com.xssdhr.service.SysUserRoleService;
import com.xssdhr.service.SysUserService;
import com.xssdhr.util.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 用户Controller控制器
 * @author xssdhr
 * @9/3/2023 下午9:06
 * @Company 南京点子跳跃传媒有限公司
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Value("${web.avatarImagesFilePath}")
    private String avatarImagesFilePath;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserService sysUserService;




    /**
     * 添加或者修改
     * @param sysUser
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:user:add')"+"||"+"hasAuthority('system:user:edit')")
    public R save(@RequestBody SysUser sysUser){
        if(sysUser.getId()==null || sysUser.getId()==-1){
            sysUser.setCreateTime(new Date());
            sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
            sysUserService.save(sysUser);
        }else{
            sysUser.setUpdateTime(new Date());
            sysUserService.updateById(sysUser);
        }
        return R.ok();
    }

    /**
     * 验证用户名
     * @param sysUser
     * @return
     */
    @PostMapping("/checkUserName")
    @PreAuthorize("hasAuthority('system:user:query')")
    public R checkUserName(@RequestBody SysUser sysUser){
        if(sysUserService.getByUsername(sysUser.getUsername())==null){
            return R.ok();
        }else{
            return R.error();
        }
    }
    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public R findById(@PathVariable(value = "id")Integer id){
        SysUser sysUser = sysUserService.getById(id);
        Map<String,Object> map=new HashMap<>();
        map.put("sysUser",sysUser);
        return R.ok(map);
    }

    /**
     头像更换功能实现
     avatar.vue
     * 修改密码
     * @param sysUser
     * @return
     */
    @PostMapping("/updateUserPwd")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R updateUserPwd(@RequestBody SysUser sysUser){
        SysUser currentUser = sysUserService.getById(sysUser.getId());
        if(bCryptPasswordEncoder.matches(sysUser.getOldPassword(),currentUser.getPassword())){
            currentUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getNewPassword()));
            currentUser.setUpdateTime(new Date());
            sysUserService.updateById(currentUser);
        }else{
            return R.error("输入旧密码错误！");
        }
        return R.ok();
    }
    /**
     * 上传用户头像图片
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadImage")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Map<String,Object> uploadImage(MultipartFile file)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        if(!file.isEmpty()){
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            String suffixName=originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName= DateUtil.getCurrentDateStr()+suffixName;
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(avatarImagesFilePath+newFileName));
            resultMap.put("code",0);
            resultMap.put("msg","上传成功");
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("title",newFileName);
            dataMap.put("src","image/userAvatar/"+newFileName);
            resultMap.put("data",dataMap);
        }
        return resultMap;
    }

    /**
     * 修改用户头像
     * @param sysUser
     * @return
     */
    @RequestMapping("/updateAvatar")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R updateAvatar(@RequestBody SysUser sysUser){
        SysUser currentUser = sysUserService.getById(sysUser.getId());
        currentUser.setUpdateTime(new Date());
        currentUser.setAvatar(sysUser.getAvatar());
        sysUserService.updateById(currentUser);
        return R.ok();
    }

    /**
     * 根据条件分页查询用户信息
     * @param pageBean
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('system:user:query')")
    public R List(@RequestBody PageBean pageBean){
        String query = pageBean.getQuery().trim();
        Page<SysUser> pageResult = sysUserService.page(new Page<>(pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<SysUser>().like(StringUtil.isNotEmpty(query),"username",query));
        //返回分页后的数据集合
        List<SysUser> userList = pageResult.getRecords();
        for(SysUser user:userList){
            //根据用户查询所有角色
            List<SysRole> roleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id=" + user.getId()));
            user.setSysRoleList(roleList);
        }
        Map<String,Object> resultMap = new HashMap<>();
        //每页数据
        resultMap.put("userList",userList);
        //总计路数
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }


    /**
     * 删除或者批量删除
     * @param ids
     * @return
     */
    @Transactional
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        sysUserService.removeByIds(Arrays.asList(ids));
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));
        return R.ok();
    }
    /**
     * 重置密码
     * @param id
     * @return
     */
    @GetMapping("/resetPassword/{id}")
    @PreAuthorize("hasAnyAuthority('system:user:edit')")
    public R resetPassword(@PathVariable(value = "id")Integer id){
        SysUser sysUser = sysUserService.getById(id);
        sysUser.setPassword(bCryptPasswordEncoder.encode(Constant.DEFAULT_PASSWORD));
        sysUser.setUpdateTime(new Date());
        sysUserService.updateById(sysUser);
        return R.ok();
    }


    /** 
     * 更新status状态
     * @param id
     * @param status
     * @return
     */
    @GetMapping("/updateStatus/{id}/status/{status}")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R updateStatus(@PathVariable(value = "id")Integer id,@PathVariable(value = "status")String status){
        SysUser sysUser = sysUserService.getById(id);
        sysUser.setStatus(status);
        sysUserService.saveOrUpdate(sysUser);
        return R.ok();
    }
    /**
     * 用户角色授权
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @Transactional
    @PostMapping("/grantRole/{userId}")
    @PreAuthorize("hasAuthority('system:user:role')")
    public R grantRole(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {
        List<SysUserRole> userRoleList = new ArrayList<>();
        Arrays.stream(roleIds).forEach(r -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(r);
            sysUserRole.setUserId(userId);
            userRoleList.add(sysUserRole);
        });
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        sysUserRoleService.saveBatch(userRoleList);
        return R.ok();
    }





}
