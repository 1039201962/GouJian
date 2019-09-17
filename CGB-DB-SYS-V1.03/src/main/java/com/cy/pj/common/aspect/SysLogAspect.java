package com.cy.pj.common.aspect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
/**
 * @Aspect 描述的类为切面类，该类中实现：
 * 1.切入点 (PointCut)的定义 ;
 * 2.通知(Advice)的定义(扩展功能);
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private SysLogService logService;
	/**
	 *@Pointcut 注解用于定义描述或定义一个切入点；切入点的定义需要遵循spring的规范；
	 *       例如:@Pointcut("bean(sysMenuServiceImpl)")为切入点的表达式的一种定义方式。
	 */
	//bean(bean名称或者一个表达式)
	//@Pointcut("bean(sysMenuServiceImpl)")

	@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
	public void logPointCut() {

	}

	/**
	 * @Around 注解描述的方法为环绕方法
	 * @param jp 连接点
	 * @return
	 * @throws Throwable
	 */
	@Around(value = "logPointCut()")
	public Object aroundAdvice(ProceedingJoinPoint jp) throws Throwable {
		long start = System.currentTimeMillis();
		log.info("start:"+start);
		Object object = jp.proceed();
		long end = System.currentTimeMillis();
		log.info("end:"+end);

		saveLog(jp,(end-start));
		log.info("spend time:"+(end-start));
		return object;
	}

	/**
	 * 获取用户行为日志
	 * @param jp
	 * @param time
	 * @throws Exception 
	 */
	private void saveLog(ProceedingJoinPoint jp, long time) throws Exception {
		
		SysLog entity = new SysLog();
		//1.获取用户行为的日志(ip,username,operation,method,params,method,createdTime)
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		//获取用户的ip地址
		String ip = request.getRemoteAddr();
		String className = jp.getTarget().getClass().toString();
		
		//----------------------------
		
		Class<?> targetCls=jp.getTarget().getClass();
		//1.2获取目标方法签名信息(包含方法名,参数列表等信息)
		//1.2.1获取方法(类名+方法名)
		MethodSignature ms=(MethodSignature)jp.getSignature();
		Method interfaceMethod=ms.getMethod();
		
		String methodName=interfaceMethod.getName();
		System.out.println("methodName:"+methodName);
		String clsMethodName=targetCls.getName()+"."+methodName;
		//1.2.2 获取方法参数(实际参数)
		System.out.println("clsMethodName="+clsMethodName);
		ObjectMapper om=new ObjectMapper();//jackson
		String params=om.writeValueAsString(jp.getArgs());//json
		//1.2.3获取注解RequiredLog
		Method targetMethod=
		targetCls.getMethod(methodName,ms.getParameterTypes());
		RequiredLog requiredLog=
		targetMethod.getAnnotation(RequiredLog.class);
		String operation=requiredLog.value();	
		
		//---------------------------
		//String  operation = requiredLog.value();	
		//传入参数
		Object[] args = jp.getArgs();
		//2.封装用户的行为日志(SysLog)
		entity.setIp(ip);
		entity.setMethod(clsMethodName);
		entity.setOperation(operation);
		entity.setParams(Arrays.toString(args));
		entity.setTime(time);
		entity.setCreatedTime(new Date());
		entity.setUsername("admin");
		System.out.println(entity);
		long t1 = System.currentTimeMillis();
		//3.调用业务层的对象方法(saveObject)将数据写入数据库
		logService.saveObject(entity);
		System.out.println("执行时长:"+(System.currentTimeMillis()-t1));
	}

}
