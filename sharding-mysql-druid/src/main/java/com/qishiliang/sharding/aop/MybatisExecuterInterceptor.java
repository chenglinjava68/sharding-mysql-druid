package com.qishiliang.sharding.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * 
 * @ClassName: MybatisExecuterIntercepto
 * @Description: AOP 基于mybatis的jdbc 设置 切片 代理，数据路由入口
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月6日 下午4:59:19
 *
 */
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MybatisExecuterInterceptor {
	
	private Logger logger = Logger.getLogger(MybatisExecuterInterceptor.class);
	
	private SQLExecute sqlExecute;

	private MybatisExecuterInterceptor() {
		sqlExecute = new SQLExecute();
	}

	@Around("execution(* org.mybatis.spring.SqlSessionTemplate.insert*(..))")
	public Object interceptInsertSQL(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		/* 执行路由检测 */
		if (isDataSource(proceedingJoinPoint)) {
			result = sqlExecute.execute(proceedingJoinPoint, true);
		} else {
			try {
				logger.debug("no need to routing");
				result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Around("execution(* org.mybatis.spring.SqlSessionTemplate.delete*(..))")
	public Object interceptDeleteSQL(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		/* 执行路由检测 */
		if (isDataSource(proceedingJoinPoint)) {
			result = sqlExecute.execute(proceedingJoinPoint, true);
		} else {
			try {
				logger.debug("no need to routing");
				result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	/**
	 * @throws Throwable 
	 * 
	 * @Title: interceptUpdateSQL 
	 * @Description: 基于Spring Aop的方式对org.springframework.jdbc.core.JdbcTemplate类下所有的update()方法进行拦截 
	 * @param @param proceedingJoinPoint 委托对象的上下文信息
	 * @param @return    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@Around("execution(* org.mybatis.spring.SqlSessionTemplate.update*(..))")
	public Object interceptUpdateSQL(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		/* 执行路由检测 */
		if (isDataSource(proceedingJoinPoint)) {
			result = sqlExecute.execute(proceedingJoinPoint, true);
		} else {
			try {
				logger.debug("no need to routing");
				result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @throws Throwable 
	 * 
	 * @Title: interceptQuerySQL 
	 * @Description: 基于Spring Aop的方式对org.springframework.jdbc.core.JdbcTemplate类下所有的query()方法进行拦截 
	 * @param @param proceedingJoinPoint
	 * @param @return    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	@Around("execution(* org.mybatis.spring.SqlSessionTemplate.select*(..))")
	public Object interceptSelectSQL(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		/* 执行路由检测 */
		if (isDataSource(proceedingJoinPoint)) {
			result = sqlExecute.execute(proceedingJoinPoint, false);
		} else {
			try {
				logger.debug("no need to routing");
				result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 
	 * @Title: isDataSource 
	 * @Description: 路由检测 
	 * @param @param proceedingJoinPoint
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	public boolean isDataSource(ProceedingJoinPoint proceedingJoinPoint) {
		//如果SqlSessionTemplate持有的不是 com.qishiliang.sharding.route.impl.DatasourceGroup 动态数据源,则不进行数据路由操作 
//		SqlSessionTemplate sqlSessionTemplate = (SqlSessionTemplate) proceedingJoinPoint.getTarget();
		return true;
	}
}