package com.qishiliang.sharding.dataSource;

/**
 * 
 * @ClassName: SetDatasource
 * @Description: 设置数据源索引
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月5日 下午9:49:53
 *
 */
public class SetDatasource {
	
	/**
	 * 
	 * @Title: setIndex 
	 * @Description: 设置数据源索引
	 * @param @param index
	 * @param @param dataSourceHolder    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public static void setDataSourceIndex(int index, DataSourceContextHolder dataSourceHolder) {
		dataSourceHolder.setDataSourceIndex(index);
	}
}