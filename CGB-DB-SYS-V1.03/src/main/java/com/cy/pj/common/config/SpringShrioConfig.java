package com.cy.pj.common.config;

import java.util.LinkedHashMap;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class SpringShrioConfig {

	@Bean
	public SecurityManager newSecurityManager(Realm realm) {
		DefaultWebSecurityManager sManager=
				 new DefaultWebSecurityManager();
				 sManager.setRealm(realm);				
				 return sManager;
	}

	@Bean("shiroFilterFactory")
	public ShiroFilterFactoryBean newShiroFilterFactoryBean(
			@Autowired SecurityManager securityManager) {
		ShiroFilterFactoryBean sfBean=
				new ShiroFilterFactoryBean();
		sfBean.setSecurityManager(securityManager);
		sfBean.setLoginUrl("/doLoginUI");
		//定义map指定请求过滤规则(哪些资源允许匿名访问,哪些必须认证访问)
		LinkedHashMap<String,String> map=
				new LinkedHashMap<>();
		//静态资源允许匿名访问:"anon"
		map.put("/bower_components/**","anon");
		map.put("/build/**","anon");
		map.put("/dist/**","anon");
		map.put("/plugins/**","anon");
		map.put("/user/doLogin","anon");
		//除了匿名访问的资源,其它都要认证("authc")后访问
		map.put("/**","authc");
		sfBean.setFilterChainDefinitionMap(map);
		return sfBean;
	}
	
	@Bean("lifecycleBeanPostProcessor")
	 public LifecycleBeanPostProcessor
	     newLifecycleBeanPostProcessor() {
		 return new LifecycleBeanPostProcessor();
	 }
	/**
 	  *  此对象会在Spring容器启动时，
	  *  扫描所有的Advisor(通知)对象
	  * ，基于Advisor对象中切入点(Pointcut)
	  * 的描述，进行代理对象的创建。
	  */
	@DependsOn("lifecycleBeanPostProcessor")
	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		
		return new DefaultAdvisorAutoProxyCreator();
	}
	
	@Bean
	 public AuthorizationAttributeSourceAdvisor 
	 authorizationAttributeSourceAdvisor(
			 SecurityManager securityManager) {
		 AuthorizationAttributeSourceAdvisor advisor=
		 new AuthorizationAttributeSourceAdvisor();
		 advisor.setSecurityManager(securityManager);
		 return advisor;
	 }
}

