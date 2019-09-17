package com.cy.pj.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@Order(1)     //数字越小优先级越高
public class SysTimeAspect {
	//1.粗粒度表达式
	//1) bean表达式 （掌握）
	//@Pointcut("bean(*ServiceImpl)") //粗粒度的切入点表达式，只能精确到类
	//@Pointcut("bean(sysMenuServiceImpl)") //首字母小写
	//2)within表达式（了解）
	//within("aop.service.UserServiceImpl")
	//2.细粒度表达式
	//1)execution表达式  （了解）
	// execution(void aop.service.UserServiceImpl.addUser())
	//2)@annotation注解（掌握）
	//@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredTime)")
	@Pointcut("bean(sysMenuServiceImpl)") //首字母小写
	 public void doTimePointCut() {
		
	}
	
	@Before("doTimePointCut()")
	public void beforeAdvice() {
		log.info("beforeAdvice()");
	}
	@After("doTimePointCut()")
	public void afterAdvice() {
		log.info("afterAdvice()");
	}
	@AfterReturning("doTimePointCut()")
	public void afterReturningAdvice() {
		log.info("afterReturningAdvice()");
	}
	@AfterThrowing("doTimePointCut()")
	public void afterThrowingAdvice() {
		log.info("afterThrowingAdvice()");
		
	}
	@Around("doTimePointCut()")
	public Object aroundAdvice(ProceedingJoinPoint jp) throws Throwable {
		log.info("aroundAdvice()");
		Object object = jp.proceed();
		log.info("aroundAdvice()");
		return object;
	}

}
