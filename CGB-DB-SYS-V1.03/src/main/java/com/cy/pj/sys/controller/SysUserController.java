package com.cy.pj.sys.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;

@RestController
@RequestMapping("/user/")
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;
	/**
	 * 查找所有的用户信息
	 * @param username
	 * @param pageCurrent
	 * @return
	 */
	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String username,Integer pageCurrent) {
		return new JsonResult(sysUserService.findPageObjects(username,pageCurrent));
	}
	@RequestMapping("doLogin")
	public JsonResult doLogin(String username,String password) {
		//封装用户的信息
		UsernamePasswordToken token =
				new UsernamePasswordToken(username,password);
		//借助subject对象提交用户信息
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		
		return new JsonResult("login ok");	
	}

	/**
	 * 禁用用户
	 * @param id
	 * @param valid
	 * @return
	 */
	@RequestMapping("doValidById")
	public JsonResult doValidById(Integer id,Integer valid) {
		
		sysUserService.validById(id,valid);
		return new JsonResult("update ok");
	}

	/**
	 * 新增用户
	 * @param user
	 * @param roleIds
	 * @return
	 */
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysUser user,Integer[] roleIds) {
		sysUserService.saveObject(user,roleIds);
		return new JsonResult("save ok");
	}

	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysUser user,Integer[] roleIds) {
		sysUserService.updateObject(user,roleIds);
		return new JsonResult("update ok");
	}

	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		Map<String, Object> map = sysUserService.findObjectById(id);
		return new JsonResult(map);
	}
	
	 @RequestMapping("doUpdatePassword")
	 @ResponseBody
	 public JsonResult doUpdatePassword(
			 String pwd,
			 String newPwd,
			 String cfgPwd) {
		 sysUserService.updatePassword(pwd, newPwd, cfgPwd);
		 return new JsonResult("update ok");
	 }



}
