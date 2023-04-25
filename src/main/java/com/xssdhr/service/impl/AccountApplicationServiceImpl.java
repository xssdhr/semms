package com.xssdhr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xssdhr.entity.AccountApplication;
import com.xssdhr.service.AccountApplicationService;
import com.xssdhr.mapper.AccountApplicationMapper;
import org.springframework.stereotype.Service;

/**
* @author xssdhr
* @description 针对表【account_application】的数据库操作Service实现
* @createDate 2023-04-18 22:04:26
*/
@Service
public class AccountApplicationServiceImpl extends ServiceImpl<AccountApplicationMapper, AccountApplication>
    implements AccountApplicationService{

}




