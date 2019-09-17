package com.cy;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.vo.SysUserDeptVo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
	@Autowired
	private SysUserDao userDao;

	@Test
	public void userDaoTests() {
		SysUserDeptVo sysUserDeptVo = userDao.findObjectById(7);
		System.out.println(sysUserDeptVo);

	}
}
