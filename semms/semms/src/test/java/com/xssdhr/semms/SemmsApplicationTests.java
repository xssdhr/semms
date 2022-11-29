package com.xssdhr.semms;

import com.xssdhr.semms.domain.Manager;
import com.xssdhr.semms.mapper.ManagerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SemmsApplicationTests {

	@Autowired
	private ManagerMapper mapper;


	@Test
	void contextLoads() {
		List<Manager> all = mapper.findAll();
		System.out.println(all);
	}

}
