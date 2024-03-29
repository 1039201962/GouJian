package com.cy.pj.sys.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class SysUser implements Serializable {
	private static final long serialVersionUID = -7065444926526118305L;
	private Integer id;
	private String username;
	private String password;//md5
	/**盐值(加密盐-辅助加密,保证密码更加安全)*/
	private String salt;
	private String email;
	private String mobile;
	/**用户状态:1表示启用,0表示禁用*/
	private Integer valid=1;
	/**用户所在部门的部门信息*/
	private Integer deptId; 
	private Date createdTime;
	private Date modifiedTime;
	private String createdUser;
	private String modifiedUser;
	
}
