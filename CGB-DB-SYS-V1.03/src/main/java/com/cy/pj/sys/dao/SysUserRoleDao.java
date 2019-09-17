package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserRoleDao {
	 @Delete("delete from sys_user_roles where user_id = #{user_id}")
	 int deleteObjectsByUserId(@Param("user_id") int user_id);
	 
	 @Delete("delete from sys_user_roles where role_id = #{role_id}")
	 int deleteObjectsByRoleId(@Param("role_id")  int role_id);
	
	 int insertObjects(@Param("user_id") int user_id,@Param("roleIds")  Integer[] roleIds);

	 List<Integer> findRoleIdsByUserId(Integer id);
}
