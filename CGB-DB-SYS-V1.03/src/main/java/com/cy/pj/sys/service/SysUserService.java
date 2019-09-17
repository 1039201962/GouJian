package com.cy.pj.sys.service;

import java.util.Map;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

public interface SysUserService {
	/**
	 * 分页查询用户的信息
	 * @param username
	 * @param pageCurrent
	 * @return
	 */
	PageObject<SysUserDeptVo> findPageObjects(String username,Integer pageCurrent);
	/**
	 * 改变用户的是否被禁用
	 * @param id
	 * @param valid
	 */
	void validById(Integer id, Integer valid);
	/**
	 * 保存用户的信息
	 * @param user
	 */
	void saveObject(SysUser user,Integer[] roleIds);
	/**
	 * 更新用户的数据
	 * @param user
	 * @param roleIds
	 */
	void updateObject(SysUser user, Integer[] roleIds);
	
	/**
	 * 根据id查询用户的信息
	 * @param id
	 */
	Map<String,Object> findObjectById(Integer id);
	/**
	 **更新用户密码
	 * @param pwd
	 * @param newPwd
	 * @param cfgPwd
	 * @return
	 */
	int updatePassword(String pwd, String newPwd, String cfgPwd);

}
