package com.cy.pj.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@RestController
@RequestMapping("/role/")
public class SysRoleController {
	
	@Autowired
	private SysRoleService service;
	
	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String name,Integer pageCurrent) {
		PageObject<Object> objects = service.findObjects(name,pageCurrent);
		return new JsonResult(objects);

	}
	
	
	@RequestMapping("doDeleteObject")
	public JsonResult doDeleteObject(int id) {
		service.deleteObjects(id);
		return new JsonResult("ok");
			
	}
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysRole entity,Integer[] menuIds) {
		service.saveObjects(entity,menuIds);
		
		return new JsonResult();
	}
	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		SysRoleMenuVo vo = service.findObjectById(id);
		
		return new JsonResult(vo);
	}
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject () {
		
		
		return new JsonResult();
	}
	@RequestMapping("doFindRoles")
	public JsonResult doFindRoles () {
			
		return new JsonResult(service.findRoles());
	}

}
