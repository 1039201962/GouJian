package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

@Mapper
public interface SysUserDao {

	SysUser findObjectByName(String username);
	/**
	 * 查询用户的信息
	 * @param username 用户姓名(可选)
	 * @param startIndex 记录起始位置
	 * @param pageSize 分页查询页面大小
	 * @return 返回一个list集合
	 */
	List<SysUserDeptVo> findPageObjects(@Param("username")String username,@Param("startIndex")int startIndex,@Param("pageSize")int pageSize);
	/**
	 * @param username 用户姓名(可选)
	 * @return 返回对应的记录条数
	 */
	int findPageCount(@Param("username")String username);
	/**
	 * 改变用户的是否被禁用
	 * @return
	 */
	int validById(@Param("id") Integer id,@Param("valid") Integer valid);
	/**
	 * 插入用户信息
	 * @param user
	 * @return
	 */
	int insertObject(SysUser user);
	/**
	 * 根据id查询用户的信息
	 * @param id
	 */
	SysUserDeptVo findObjectById(Integer id);
	
	/**
	 *    根据id删除用户信息
	 * @param id
	 * @return
	 */
	int deleteObjectById(Integer id);
	/**
	 ** 更新用户的密码
	 * @param id
	 * @param newPwd
	 * @return
	 */
	int updatePassword(@Param("id")Integer id,@Param("password") String password,@Param("salt")String salt);
	

}
