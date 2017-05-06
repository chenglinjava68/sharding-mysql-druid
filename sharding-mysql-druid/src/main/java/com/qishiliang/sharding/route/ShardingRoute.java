package com.qishiliang.sharding.route;

import org.apache.log4j.Logger;

import com.qishiliang.sharding.dataSource.DataSourceContextHolder;
import com.qishiliang.sharding.dataSource.SetDatasource;
import com.qishiliang.sharding.parser.ResolveRouteValue;
import com.qishiliang.sharding.parser.ResolveTableName;
import com.qishiliang.sharding.route.tb.SetTableName;
import com.qishiliang.sharding.utils.ShardingUtil;

/**
 * 
 * @ClassName: ShardingRoute
 * @Description: 数据库分库分表 实现类
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月6日 下午4:42:37
 *
 */
public class ShardingRoute extends RouteImpl {
	
	private static Logger logger = Logger.getLogger(ShardingRoute.class);
	
	private DataSourceContextHolder dataSourceContextHolder;

	public ShardingRoute() {
		dataSourceContextHolder = DataSourceContextHolder.getDataSourceHolder();
	}

	@Override
	public Object[] route(String srcSql, Object[] params, boolean sqlType) {

		String targetSql = null;

		//数据库数量
		final int dbQuantity = shardingConfigInfo.getDbQuantity();
		//表数量
		final int tbQuantity = shardingConfigInfo.getTbQuantity();

		/* 解析sql语句中的路由条件 */
		final Object shardingRule = ResolveRouteValue.getRoute(srcSql);
		logger.debug("源sql为："+srcSql+" 获取的路由条件为："+shardingRule);

		Rule dbRule = super.getDbRule();

		int dbIndex = dbRule.getRouteIndex(shardingRule, dbQuantity, tbQuantity);

		Rule tbRule = super.getTbRule();
		int tbIndex = tbRule.getRouteIndex(shardingRule, dbQuantity, tbQuantity);

		/* 解析数据库表名 */
		final String tbName = ResolveTableName.getTableName(srcSql);
		/* 单库多表模式下设定真正的数据库表名 */
		targetSql = SetTableName.setRouteTableName(shardingConfigInfo, dbIndex, tbIndex, dbQuantity, tbQuantity, tbName, srcSql);

		final int beginIndex = ShardingUtil.getBeginIndex(sqlType);

		/* 切换数据源索引 */
		SetDatasource.setDataSourceIndex((dbIndex + beginIndex), dataSourceContextHolder);

		return updateParam(targetSql, params);
	}
}