package com.qishiliang.sharding.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.qishiliang.sharding.exceptions.SqlParserException;
import com.qishiliang.sharding.route.ShardingConfigInfo;

public class ShardingUtil {

	private static Logger logger = Logger.getLogger(ShardingUtil.class);

	private static ShardingConfigInfo shardingConfigInfo;

	static{
		shardingConfigInfo = ShardingConfigInfo.getShardInfo();
	}

	public static String getDBTBIndex(String key, int dbSize, int tbSize, String dbName, String tbName){
		int dbIndex = ShardingUtil.getDataBaseIndex(key, dbSize, tbSize);
		int tbIndex = ShardingUtil.getTableIndex(key, dbSize, tbSize);

		String result = dbName+"_"+CommonUtil.completionNumberFormat(dbIndex)+"."+tbName+"_"+CommonUtil.completionNumberFormat(tbIndex);

		return "路由规则为：--> "+result;
	}

	/**
	 * 
	 * @Title: getRouteName 
	 * @Description: 根据源名称 设置符合路由规则的名称 
	 * @param @param index
	 * @param @param srcName
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static String getRouteName(int index, String srcName) {
		String targetName = srcName + "_" + CommonUtil.completionNumberFormat(index);
		return targetName;
	}

	/**
	 * 
	 * @Title: getDataBaseIndex 
	 * @Description: 根据分库规则计算出数据源索引  TODO 可根据自己的规则进行定制
	 * @param @param shardKey
	 * @param @param dbQuantity
	 * @param @param tbQuantity
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public static int getDataBaseIndex(Object shardKey, int dbQuantity, int tbQuantity) {
		Long routeKey = CommonUtil.crc32(CommonUtil.toByteArray(shardKey));
		// shardKey % tbSize / dbSize 
		return (int) ((routeKey % tbQuantity) / dbQuantity);
	}

	/**
	 * 
	 * @Title: getTbIndex 
	 * @Description: 根据分片规则计算出数据库表索引  TODO 可根据自己的规则进行定制
	 * @param @param shardKey 路由键值
	 * @param @param dbQuantity 数据库数量
	 * @param @param tbQuantity 数据表数量
	 * @param @param shardMode 是否分片
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public static int getTableIndex(Object shardKey, int dbQuantity, int tbQuantity) {
		Long routeKey = CommonUtil.crc32(CommonUtil.toByteArray(shardKey));
		// shardKey % tbSize % dbSize 
		return (int) (routeKey % tbQuantity % dbQuantity);

	}

	/**
	 * 
	 * @Title: getRouteValue 
	 * @Description: 根据where条件后的sql语句，解析 第一个参数的值 作为路由key的值 
	 * @param @param where
	 * @param @return    设定文件 
	 * @return long    返回类型 
	 * @throws
	 */
	public static Object getRouteValue(SQLExpr where){

		List<String> columns = getConditionList(where);

		if(columns.size() < 2){
			throw new SqlParserException("no codition");
		}

		return columns.get(2);
	}

	public static List<String> getConditionList(SQLExpr where){
		List<String> columns = null;
		if (null == where) {
			throw new SqlParserException("no condition");
		} else {
			columns = Arrays.asList(where.toString().split("\\s"));
		}

		return columns;
	}

	/**
	 * 
	 * @Title: containWhere 
	 * @Description: 判断sql是否包含 where条件语句，true 为包含 ，否则抛出异常 
	 * @param @param sql
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	public static boolean containWhere(String sql) {

		SQLStatementParser parser = new MySqlStatementParser(sql);
		List<SQLStatement> statements = parser.parseStatementList();

		if (!statements.isEmpty()) {
			SQLStatement statement = statements.get(0);
			if (statement instanceof SQLSelectStatement) {
				SQLSelectStatement selectStatement = (SQLSelectStatement) statement;
				SQLSelect sqlselect = selectStatement.getSelect();
				SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) sqlselect.getQuery();
				try {
					queryBlock.getWhere();
					return true;
				} catch (SqlParserException e) {
					throw new SqlParserException("no condition");
				}
			} else if (statement instanceof SQLInsertStatement) {
				/* INSERT语句直接放行 */
				return true;

			} else if (statement instanceof SQLDeleteStatement) {
				SQLDeleteStatement deleteStatement = (SQLDeleteStatement) statement;
				try {
					deleteStatement.getWhere();
					return true;

				} catch (SqlParserException e) {
					throw new SqlParserException("no condition");
				}
			} else if (statement instanceof SQLUpdateStatement) {
				SQLUpdateStatement updateStatement = (SQLUpdateStatement) statement;
				try {
					updateStatement.getWhere();
					return true;

				} catch (SqlParserException e) {
					throw new SqlParserException("no condition");
				}
			}
		}
		return false;
	}


	/**
	 * 
	 * @Title: getBeginIndex 
	 * @Description: 获取master/slave的数据源起始索引
	 * @param  wr_index 读写起始索引,如比r0w512
	 * @param  operation 
	 * 					true为master起始索引,false为slave起始索引
	 * @return int    返回类型  起始索引
	 * @throws
	 */
	public static int getBeginIndex(boolean sqlType) {

		String write_index = shardingConfigInfo.getWrite_index();
		String read_index = shardingConfigInfo.getRead_index();

		Integer index ;
		if (sqlType){// 写库
			logger.debug("路由数据库为写库");
			index = Integer.parseInt(write_index);
		} else {// 读库
			logger.debug("路由数据库为读库");
			index = Integer.parseInt(read_index);
		}

		return index;
	}
}
