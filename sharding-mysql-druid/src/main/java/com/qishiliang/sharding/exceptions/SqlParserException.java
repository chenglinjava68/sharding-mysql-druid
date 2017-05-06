package com.qishiliang.sharding.exceptions;


/**
 * 
 * @ClassName: SqlParserException
 * @Description: sql运行时异常
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年5月6日 上午10:27:17
 *
 */
public class SqlParserException extends RuntimeException {
	private static final long serialVersionUID = -3671607184598679934L;

	public SqlParserException(String str) {
		super(str);
	}
}