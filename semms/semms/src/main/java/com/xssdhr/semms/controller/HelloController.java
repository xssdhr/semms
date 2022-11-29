package com.xssdhr.semms.controller;


import com.xssdhr.semms.domain.Manager;
import com.xssdhr.semms.service.ManagerService;
import io.netty.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hello")
//@CrossOrigin(origins = "http://127.0.0.1:8080/")
@CrossOrigin(origins = "http://127.0.0.1:8080/", maxAge = 3600,allowCredentials = "true")
public class HelloController {


    @Autowired
    private ManagerService managerService;

    @RequestMapping("/findAll")
    public List<Manager> findAll(){
        //调用service查询数据
        return managerService.findAll();
    }
}
