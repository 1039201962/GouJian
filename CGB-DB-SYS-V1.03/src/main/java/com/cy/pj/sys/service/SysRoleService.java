package com.cy.pj.sys.service;

import java.util.List;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

public interface SysRoleService {
	
	
	PageObject<Object> findObjects(String name,Integer pageCurrent);
	
	List<SysRole> findRoles();

	int deleteObjects(Integer id);

	int saveObjects(SysRole entity, Integer[] menuIds);

	SysRoleMenuVo findObjectById(Integer id);
	

}
