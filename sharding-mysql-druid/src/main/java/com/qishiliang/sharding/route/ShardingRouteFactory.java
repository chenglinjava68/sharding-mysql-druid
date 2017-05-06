package com.qishiliang.sharding.route;

/**
 * 
 * @ClassName: HorizontalFacadeFactory
 * @Description: 水平分库,水平分片模式的工厂类
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月6日 下午4:34:29
 *
 */
public class ShardingRouteFactory implements RouteFactory {
	@Override
	public Route getRoute() {
		return new ShardingRoute();
	}
}