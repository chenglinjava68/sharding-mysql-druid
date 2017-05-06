package com.qishiliang.sharding.route.db;

import com.qishiliang.sharding.route.Rule;
import com.qishiliang.sharding.route.RuleFactory;

/**
 * 
 * @ClassName: DbRuleFactory
 * @Description: 分库路由规则工厂类
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月5日 下午9:15:28
 *
 */
public class DbRuleFactory implements RuleFactory{
	@Override
	public Rule getRule() {
		return new DbRule();
	}
}