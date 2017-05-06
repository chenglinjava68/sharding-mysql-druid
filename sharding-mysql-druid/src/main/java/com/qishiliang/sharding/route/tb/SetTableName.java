package com.qishiliang.sharding.route.tb;

import org.apache.log4j.Logger;

import com.qishiliang.sharding.route.ShardingConfigInfo;
import com.qishiliang.sharding.utils.ShardingUtil;

/**
 * 
 * @ClassName: SetTableName
 * @Description: 数据路由前重设数据库表名,比如通用的表名为tab,那么重设后为tab_0000
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月5日 下午9:50:18
 *
 */
public class SetTableName {
	
	private static Logger logger = Logger.getLogger(SetTableName.class);

	/**
	 * 
	 * @Title: setName 
	 * @Description: 多库多表模式下重设真正的数据库表名 
	 * @param @param shardingConfigInfo 分库分表配置信息
	 * @param @param dbIndex 数据源索引
	 * @param @param tbIndex 数据库表索引
	 * @param @param dbSize 配置文件中数据库的数量
	 * @param @param tbSize 配置文件中数据库表的数量
	 * @param @param tbName 数据库通用表名
	 * @param @param srcSql
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static String setRouteTableName(ShardingConfigInfo shardingConfigInfo, int dbIndex, int tbIndex, int dbSize, int tbSize,
			String tbName, String srcSql) {
		int tbIndexInDb = -1;
		if (shardingConfigInfo.isConsistent()) {//表连续
			/* 计算平均每个数据库的表的数量 */
			int tbSizeInDb = tbSize / dbSize;
			/* 计算数据库表在指定数据库的索引,其算法为(库索引 * 平均每个数据库的表的数量 + 表索引) */
			tbIndexInDb = dbIndex * tbSizeInDb + tbIndex;
		} else {// 表不连续，即 每个库中表的序号是一致的
			tbIndexInDb = tbIndex;
		}
		final String newTableName = ShardingUtil.getRouteName(tbIndexInDb, tbName);

		String targetSql = srcSql.replaceFirst(tbName, newTableName);
		logger.debug("多库多表模式下，用于执行的正式sql为：--> "+targetSql);
		return targetSql;
	}

	/**
	 * 
	 * @Title: setName 
	 * @Description: 单库多表模式下设定真正的数据库表名 
	 * @param @param shardingConfigInfo 分库分表配置信息
	 * @param @param tbIndex 数据库表索引
	 * @param @param tbName 数据库通用表名
	 * @param @param sql
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	protected static String setRouteTableName(int tbIndex, String tbName, String sql) {
		
		final String newTableName = ShardingUtil.getRouteName(tbIndex, tbName);
		
		String targetSql = sql.replaceFirst(tbName, newTableName);
		logger.debug("单库多表模式下，用于执行的正式sql为：--> "+targetSql);
		return targetSql;
	}
}