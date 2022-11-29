package com.xssdhr.semms.service.impl;

import com.xssdhr.semms.domain.Manager;
import com.xssdhr.semms.mapper.ManagerMapper;
import com.xssdhr.semms.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public List<Manager> findAll() {
        return managerMapper.findAll();
    }
}
