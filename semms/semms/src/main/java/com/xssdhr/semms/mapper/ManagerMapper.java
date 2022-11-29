package com.xssdhr.semms.mapper;


import com.xssdhr.semms.domain.Manager;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerMapper  {
    List<Manager> findAll();
}