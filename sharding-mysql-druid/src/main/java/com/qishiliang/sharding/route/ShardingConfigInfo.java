package com.qishiliang.sharding.route;

import org.apache.log4j.Logger;

/**
 * 
 * @ClassName: ShardConfigInfo
 * @Description: 分库分表配置信息
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月5日 下午9:50:51
 *
 */
public class ShardingConfigInfo {
	
	private static Logger logger = Logger.getLogger(ShardingConfigInfo.class);
	
	private static ShardingConfigInfo shardingConfigInfo;
	
	private String write_index; //数据库写库 索引 
	private String read_index; //数据库读库索引 
	private Integer dbQuantity;// 数据库数量
	private Integer tbQuantity;// 数据表数量
	private boolean consistent; //表名是否连续,true为连续,false为不连续
	
	static {
		if(shardingConfigInfo == null){
			logger.debug("初始化 分库分表规则");
			shardingConfigInfo = new ShardingConfigInfo();
		}else{
			logger.debug("分库分表规则已被初始化");
		}
	}

	private ShardingConfigInfo() {}

	public static ShardingConfigInfo getShardInfo() {
		return shardingConfigInfo;
	}

	public String getWrite_index() {
		return write_index;
	}

	public void setWrite_index(String write_index) {
		this.write_index = write_index;
	}

	public String getRead_index() {
		return read_index;
	}

	public void setRead_index(String read_index) {
		this.read_index = read_index;
	}

	public Integer getDbQuantity() {
		return dbQuantity;
	}

	public void setDbQuantity(Integer dbQuantity) {
		this.dbQuantity = dbQuantity;
	}

	public Integer getTbQuantity() {
		return tbQuantity;
	}

	public void setTbQuantity(Integer tbQuantity) {
		this.tbQuantity = tbQuantity;
	}

	public boolean isConsistent() {
		return consistent;
	}

	public void setConsistent(boolean consistent) {
		this.consistent = consistent;
	}

}