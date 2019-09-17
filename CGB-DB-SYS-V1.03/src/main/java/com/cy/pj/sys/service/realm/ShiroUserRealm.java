package com.cy.pj.sys.service.realm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
@Service
public class ShiroUserRealm extends AuthorizingRealm {
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private SysRoleMenuDao roleMenuDao;
	@Autowired
	private SysUserRoleDao userRoleDao;
	@Autowired
	private SysMenuDao menuDao;
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		//构建凭证匹配对象
		HashedCredentialsMatcher cMatcher=
				new HashedCredentialsMatcher();
		//设置加密算法
		cMatcher.setHashAlgorithmName("MD5");
		//设置加密次数
		cMatcher.setHashIterations(1);
		super.setCredentialsMatcher(cMatcher);

	}
	
	/**
	 ** 授权鉴定
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 1.获取用户名
		UsernamePasswordToken upToken=(UsernamePasswordToken)token;
		String username = upToken.getUsername();
		// 2 基于用户名
		SysUser sysUser = userDao.findObjectByName(username);
		// 3.判定用是否存在
		if(sysUser==null) throw new UnknownAccountException();
		// 4.判断是被禁用
		if(sysUser.getValid()==0) throw new LockedAccountException();
		// 5.封装用户的信息，传递给认证管理器进行认证
		ByteSource credentialsSalt=
				ByteSource.Util.bytes(sysUser.getSalt());
		SimpleAuthenticationInfo info=
				new SimpleAuthenticationInfo(
						sysUser,//principal身份
						sysUser.getPassword(),//hashedCredentials
						credentialsSalt,//credentialsSalt
						getName());//realmName
		return info;//返回给SecurityManager

	}
	/**
	 ** 负责授权信息的获取和封装
	 * AuthorizationException 授权异常
	 */
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		//1.获取登陆用户的信息
		SysUser user = (SysUser)principals.getPrimaryPrincipal();
		//2.基于用户的id查询用户的角色id
		List<Integer> roleIds = userRoleDao.findRoleIdsByUserId(user.getId());
		if(roleIds == null || roleIds.size() == 0) 
			throw new AuthorizationException();
		//3.基于角色id查询菜单id
		List<Integer> menuIds = roleMenuDao.findMenuIdsByRoleIds((Integer[]) roleIds.toArray());
		if (menuIds == null || menuIds.size()==0) 
			throw new AuthorizationException();
		//4.基于菜单id查询菜单权限的标识
		List<String> permissions = menuDao.findPermissions((Integer[]) menuIds.toArray());
		if(permissions == null || permissions.size() == 0) 
			throw new AuthorizationException();
		//5.封装查询结果返回
		HashSet<String> perssionSet = new HashSet<>();
		for (Iterator iterator = permissions.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if(!string.isEmpty()) {
				permissions.add(string);
			}			
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(perssionSet);		
		return info;
	}



}
