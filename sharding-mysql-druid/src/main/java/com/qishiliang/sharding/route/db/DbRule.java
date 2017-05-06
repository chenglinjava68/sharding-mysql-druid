package com.qishiliang.sharding.route.db;

import com.qishiliang.sharding.route.Rule;
import com.qishiliang.sharding.utils.ShardingUtil;

/**
 * 
 * @ClassName: DbRule
 * @Description: 解析分库规则后计算数据源索引
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月5日 下午4:01:02
 *
 */
public class DbRule implements Rule{

	@Override
	public int getRouteIndex(Object routeValue, int dbQuantity, int tbQuantity) {
		int dbIndex = ShardingUtil.getDataBaseIndex(routeValue, dbQuantity, tbQuantity);
		return dbIndex;
	}
}