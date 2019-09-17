package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

/**
 * Role Dao接口
 * @author GouJian
 *
 */
@Mapper
public interface SysRoleDao {
	
	List<SysRole> findObjects(@Param("name")String name,@Param("startIndex")int startIndex,@Param("pages") int pages);
	
	int getRowsCount(@Param("name")String name);

	int deleteObjectsByRoleId(@Param("ids")int...ids);
	
	int insertObject(SysRole entity);
	
	int updataObject(SysRole entity);

	SysRoleMenuVo findObjectById(@Param("id")Integer id);

	List<SysRole> findAllObjects();

}
