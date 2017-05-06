package com.qishiliang.sharding.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import com.qishiliang.sharding.dataSource.DataSourceContextHolder;
import com.qishiliang.sharding.dataSource.SetDatasource;
import com.qishiliang.sharding.route.Route;
import com.qishiliang.sharding.route.RouteFactory;
import com.qishiliang.sharding.route.ShardingConfigInfo;
import com.qishiliang.sharding.route.ShardingRouteFactory;
import com.qishiliang.sharding.utils.ShardingUtil;

/**
 * 
 * @ClassName: SQLExecute
 * @Description: 在sql执行之前进行数据路由
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月6日 上午10:21:32
 *
 */
public class SQLExecute {

	private Logger logger = Logger.getLogger(SQLExecute.class);

	private ShardingConfigInfo shardingConfigInfo;
	private DataSourceContextHolder dataSourceHolder;
	private Route shardingRoute;

	public SQLExecute() {
		shardingConfigInfo = ShardingConfigInfo.getShardInfo();
		dataSourceHolder = DataSourceContextHolder.getDataSourceHolder();

		RouteFactory shardingRouteFactory = new ShardingRouteFactory();

		shardingRoute = shardingRouteFactory.getRoute();
	}

	/**
	 * @throws Throwable 
	 * 
	 * @Title: execute 
	 * @Description: 利用aop原理,进行数据路由 
	 * @param @param proceedingJoinPoint	委托对象(即:目标对象/被代理的对象)的上下文信息
	 * @param @param isWrite	true为master起始索引,false为slave起始索引,读写分离
	 * @param @return    设定文件 
	 * @return Object    返回类型 
	 * @throws
	 */
	public Object execute(ProceedingJoinPoint proceedingJoinPoint, boolean sqlType) throws Throwable {
		Object obj = null;
		if (null != proceedingJoinPoint) {

			//获取切片中的相关信息
			Object[] params = proceedingJoinPoint.getArgs();

			if (0 >= params.length)
				return obj;

			Object param = params[0];

			if (param instanceof String) {
				String sql = param.toString();
				logger.debug("before sql-->" + sql);
				
				// 通过参数判断分库分表规则
				if (shardingConfigInfo.getDbQuantity() > 1){// 多库 分表

					params = shardingRoute.route(sql, params, sqlType);

					sql = params[0].toString();
					logger.debug("after sql-->" + sql);

				} else {// 单库 分表
					final int index = ShardingUtil.getBeginIndex(sqlType);
					SetDatasource.setDataSourceIndex(index, dataSourceHolder);
				}
			}
			try {
				//继续执行
				obj = proceedingJoinPoint.proceed(params);
			} catch (Throwable e) {
				logger.error(e.getMessage());
				throw e;
			}
		}
		return obj;
	}
}