package com.qishiliang.sharding.route;

/**
 * 
 * @ClassName: Route
 * @Description: 分库分表路由模式接口
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月6日 上午10:09:15
 *
 */
public interface Route {
	
	/**
	 * 
	 * @Title: route 
	 * @Description: 数据路由 
	 * @param @param sql
	 * @param @param params
	 * @param @param indexType
	 * @param @return    设定文件 
	 * @return Object[]    返回类型 
	 * @throws
	 */
	public Object[] route(String sql, Object[] params, boolean sqlType);
}