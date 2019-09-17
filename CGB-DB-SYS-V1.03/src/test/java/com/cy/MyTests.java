package com.cy;

import java.util.List;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.service.SysUserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTests {
	@Autowired
	private SysUserDao userdao;
	@Autowired
	private SysRoleDao roledao;
	
	
	@Autowired
	private SysRoleService service;
	@Test
	public void testUserDao() {
		
		
	}
	@Test
	public void testRoleDao() {
		int rows = roledao.getRowsCount(null);
		System.out.println(rows);
		
	}
	
	@Test
	public void testRoleService() {
		PageObject<Object> object = service.findObjects("系统",1);
		System.out.println(object);
	}
	
	@Test
	public void Test1() {
		String salt = "089fa0e8-2884-42a3-af85-9e849a989f90";
		SimpleHash hash = new SimpleHash("MD5","123456",salt);
		System.out.println(hash.toHex());
	}

}
