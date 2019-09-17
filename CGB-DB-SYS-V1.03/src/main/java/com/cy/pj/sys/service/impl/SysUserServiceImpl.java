package com.cy.pj.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {
	/**
	 * pageSize 分页查询页面的大小
	 */
	private final static int pageSize = 5;
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private SysUserRoleDao userRoleDao;
	@Override
	public PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent) {
		long start = System.currentTimeMillis();
		//1.参数检验
		if(pageCurrent<1) throw new IllegalArgumentException("页面参数异常");
		//2.计算起始位置
		int startIndex = (pageCurrent-1)*pageSize;
		//3.查询满足记录的条数
		int rowCount = userDao.findPageCount(username);
		if(rowCount<1) throw new ServiceException("没有对应的记录");
		//4.查询对应的用户信息
		List<SysUserDeptVo> list = userDao.findPageObjects(username, startIndex, pageSize);
		//5.返回一个封装了查询结果的信息
		long time = System.currentTimeMillis()-start;
		log.info("spendTime:"+time);
		return new PageObject<>(rowCount,list,pageCurrent,pageSize);
	}


	@RequiredLog(value = "禁用启用")
	@Override
	public void validById(Integer id, Integer valid) {
		//1.参数校验
		if(id==null || id <1) new IllegalArgumentException("用户id参数异常");
		//2.调用dao层的方法对数据更新
		int rows = userDao.validById(id, valid);
		//3.对结果进行处理
		if(rows < 1) throw new ServiceException("服务数据异常");	
	}


	@Override
	public void saveObject(SysUser user,Integer[] roleIds) {
		//1.参数校验
		if(user==null) throw new ServiceException("用户信息不能为空");
		if(user.getUsername()==null || user.getUsername()=="") 
			throw new ServiceException("用户名不能为空");
		if(user.getPassword()==null || user.getPassword()=="") 
			throw new ServiceException("密码不能为空");
		if(user.getDeptId()==null ) 
			throw new ServiceException("部门未选择");
		if(roleIds.length==0)
			throw new ServiceException("至少为用户分配一种权限");
		//2.往user表里插入用户的基本信息

		//2.1获取加密的盐度
		String salt=UUID.randomUUID().toString();
		//2.2 加密
		SimpleHash SH = new SimpleHash("MD5", user.getPassword(), salt);
		//2.3 将加密后的密码设置给user
		user.setPassword(SH.toHex());
		user.setSalt(salt);
		//2.4 将用户信息插入到用户表里
		try {
			userDao.insertObject(user);
		} catch (Exception e) {
			throw new ServiceException("用户名已存在");
		}
		//2.5 更新用户角色关系表
		userRoleDao.insertObjects(user.getId(), roleIds);

	}


	@Override
	public void updateObject(SysUser user, Integer[] roleIds) {
		//校验数
		if(user==null) throw new IllegalArgumentException("用户信息异常");
		try {
			userDao.deleteObjectById(user.getId());
			userRoleDao.deleteObjectsByUserId(user.getId());
			userDao.insertObject(user);
			userRoleDao.insertObjects(user.getId(), roleIds);
		} catch (Exception e) {
			throw new ServiceException("数据更新异常");
		}		
	}
	/**
	 * Cacheable 描述业务方法时
	 */
	@Cacheable(value = "userCache")
	@Override
	public Map<String, Object> findObjectById(Integer id) {
		System.out.println("findById()......");
		//1.参数校验
		if(id==null || id <1) throw new IllegalArgumentException("无效的参数异常");
		//2.根据id查询用户的基本信息
		SysUserDeptVo user = userDao.findObjectById(id);
		//3.根据用户id查询用户的角色信息
		List<Integer> roleIds = userRoleDao.findRoleIdsByUserId(id);
		//4.封装信息到map
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}


	@Override
	public int updatePassword(String pwd, String newPwd, String cfgPwd) {
		/*
		 * System.out.println("原密码："+pwd); System.out.println("新密码："+newPwd);
		 * System.out.println("重复密码："+cfgPwd);
		 */
		//1.判断新密码是否为空
		if(StringUtils.isEmpty(newPwd)||StringUtils.isEmpty(cfgPwd))
			throw new IllegalArgumentException("密码不能为空");
		//2.判断新旧密码是否一致
		if(pwd.equals(newPwd)) 
			throw new IllegalArgumentException("不能与当前密码一样");
		//3.判断两次输入密码是否一致
		if(!newPwd.equals(cfgPwd))
			throw new IllegalArgumentException("两次密码不一致");
		//4.判断原密码是否为空
		if(StringUtils.isEmpty(pwd))
			throw new IllegalArgumentException("原密码不能为空");
		//5.获取当前用户的信息
		SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
		SimpleHash sh = new SimpleHash("MD5",pwd, user.getSalt(), 1);
		if(!sh.toHex().equals(user.getPassword())) 
			throw new IllegalArgumentException("原密码错误,验证失败");
		String salt=UUID.randomUUID().toString();
		SimpleHash sh2 = new SimpleHash("MD5", newPwd, salt);
		
		
		int i = userDao.updatePassword(user.getId(),sh2.toHex(),salt);
		if(i==0) throw new ServiceException("密码更新失败");
		return i;
	}	
}
