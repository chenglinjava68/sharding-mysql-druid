package com.qishiliang.sharding.route.tb;

import com.qishiliang.sharding.route.Rule;
import com.qishiliang.sharding.utils.ShardingUtil;

/**
 * 
 * @ClassName: TbRule
 * @Description: 解析分表规则后计算分表索引
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月7日 上午10:20:36
 *
 */
public class TbRule implements Rule{

	@Override
	public int getRouteIndex(Object routeValue, int dbQuantity, int tbQuantity) {
		return ShardingUtil.getTableIndex(routeValue, dbQuantity, tbQuantity);
	}
}