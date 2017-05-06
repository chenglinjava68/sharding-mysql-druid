package com.qishiliang.sharding.dataSource;

/**
 * 
 * @ClassName: DataSourceContextHolder
 * @Description: 数据源上下文持有
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月8日 上午9:53:19
 *
 */
public class DataSourceContextHolder {

	//通过ThreadLocal 每个线程维护一个数据源
	private final ThreadLocal<Integer> dataSourceContextHolder;
	
	private static DataSourceContextHolder dataSourceHolder;

	static {
		dataSourceHolder = new DataSourceContextHolder();
	}

	private DataSourceContextHolder() {
		dataSourceContextHolder = new ThreadLocal<Integer>();
	}

	public static DataSourceContextHolder getDataSourceHolder() {
		return dataSourceHolder;
	}

	//设置数据源索引
	public void setDataSourceIndex(int index) {
		dataSourceContextHolder.set(index);
	}

	//获取数据源索引
	public int getDataSourceIndex() {
		return dataSourceContextHolder.get();
	}
	
	//删除索引
	public void clearDataSourceIndex(){
		dataSourceContextHolder.remove();
	}
}