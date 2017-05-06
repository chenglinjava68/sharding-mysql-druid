package com.qishiliang.sharding.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;

import com.qishiliang.sharding.dataSource.DatasourceGroup;

/**
 * 
 * @ClassName: SQLExecuterInterceptor
 * @Description: AOP 基于spring的jdbc 设置 切片 代理，数据路由入口
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月6日 下午4:59:19
 *
 */
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class JDBCExecuterInterceptor {
	
	private Logger logger = Logger.getLogger(JDBCExecuterInterceptor.class);
	
	private SQLExecute sqlExecute;

	private JDBCExecuterInterceptor() {
		sqlExecute = new SQLExecute();
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
	@Around("execution(* org.springframework.jdbc.core.JdbcTemplate.update*(..))")
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
	@Around("execution(* org.springframework.jdbc.core.JdbcTemplate.query*(..))")
	public Object interceptQuerySQL(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
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
		//	如果JdbcTemplate持有的不是 com.qishiliang.sharding.route.impl.DatasourceGroup 动态数据源,则不进行数据路由操作 
		return ((JdbcTemplate) proceedingJoinPoint.getTarget()).getDataSource() instanceof DatasourceGroup;
	}
}