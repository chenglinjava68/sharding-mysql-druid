package com.qishiliang.sharding.parser;

import java.util.List;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

/**
 * 
 * @ClassName: ResolveTableName
 * @Description: 解析数据库表名
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月5日 下午9:24:04
 *
 */
public class ResolveTableName {
	/**
	 * 
	 * @Title: getTbName 
	 * @Description: 解析数据库表名 
	 * @param @param sql
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static String getTableName(String sql) {
		StringBuffer tbName = new StringBuffer();
		
		/* 生成AST抽象语法树 */
		SQLStatementParser parser = new MySqlStatementParser(sql);
		List<SQLStatement> statements = parser.parseStatementList();
		
		if (!statements.isEmpty()) {
			SQLStatement statement = statements.get(0);
			
			/* 将AST输出为字符串 */
			SQLASTOutputVisitor outputVisitor = new MySqlOutputVisitor(tbName);
			
			if (statement instanceof SQLSelectStatement) {//选择语句
				SQLSelectStatement selectStatement = (SQLSelectStatement) statement;
				SQLSelect sqlselect = selectStatement.getSelect();
				SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) sqlselect.getQuery();
				queryBlock.getFrom().accept(outputVisitor);
				
			} else if (statement instanceof SQLInsertStatement) {//插入语句
				SQLInsertStatement insertStatement = (SQLInsertStatement) statement;
				insertStatement.getTableName().accept(outputVisitor);
				
			} else if (statement instanceof SQLDeleteStatement) {//删除语句
				SQLDeleteStatement deleteStatement = (SQLDeleteStatement) statement;
				deleteStatement.getTableName().accept(outputVisitor);
				
			} else if (statement instanceof SQLUpdateStatement) {//更新语句
				SQLUpdateStatement updateStatement = (SQLUpdateStatement) statement;
				updateStatement.getTableName().accept(outputVisitor);
			}
		}
		
		return tbName.toString().split("\\s")[0];
	}

}