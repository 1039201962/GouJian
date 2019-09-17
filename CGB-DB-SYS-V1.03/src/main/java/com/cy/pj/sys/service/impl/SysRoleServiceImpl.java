package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@Service
public class SysRoleServiceImpl implements SysRoleService {
	
	private static final int pages = 5;
	
	@Autowired
	private SysRoleDao RoleDao;
	@Autowired
	private SysUserRoleDao  UserRoleDao;
	@Autowired
	private SysRoleMenuDao RoleMenuDao;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public PageObject findObjects(String name,Integer pageCurrent) {
		if(pageCurrent==null||pageCurrent<1) throw new IllegalArgumentException("无效的请求参数");
		int startIndex = (pageCurrent-1)*pages;
		List<SysRole> list = RoleDao.findObjects(name,startIndex, pages);
		if(list.size()==0) throw new ServiceException("没有对应的数据");
		int rows = RoleDao.getRowsCount(name);
		System.out.println(list);
		return new PageObject<SysRole>(rows,list,pageCurrent,pages) ;
	}

/**
 *   p:删除角色之前要先删除,与角色相关的菜单信息
 */
	@Override
	public int deleteObjects(Integer id) {
		if(id<1) throw new IllegalArgumentException("参数异常");
		//删除用户角色关系
		UserRoleDao.deleteObjectsByRoleId(id);
		//删除角色菜单关系
		RoleMenuDao.deleteObjectsByRoleId(id);
		//删除角色信息
		int rows = RoleDao.deleteObjectsByRoleId(id);
		if(rows==0) throw new ServiceException("记录已经不存在了");
		return rows;
	}

@Override
public int saveObjects(SysRole entity, Integer[] menuIds) {
	if(entity==null) throw new IllegalArgumentException("参数无效");
	if(entity.getName().isEmpty()) throw new IllegalArgumentException("用户名不能为空");
	if(menuIds.length==0) throw new ServiceException("至少赋予一种权限");
	RoleDao.insertObject(entity);
	return 0;
}

@Override
public SysRoleMenuVo findObjectById(Integer id) {
	if(id==null||id<1) throw new IllegalArgumentException("无效的参数异常");
	SysRoleMenuVo rm = RoleDao.findObjectById(id);
	if(rm==null) throw new ServiceException("记录可能不存在");
	return rm;	
}

@Override
public List<SysRole> findRoles() {
	
	return RoleDao.findAllObjects();	
}

}
