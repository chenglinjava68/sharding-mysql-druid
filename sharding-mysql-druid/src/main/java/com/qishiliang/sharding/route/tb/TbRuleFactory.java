package com.qishiliang.sharding.route.tb;

import com.qishiliang.sharding.route.Rule;
import com.qishiliang.sharding.route.RuleFactory;

/**
 * 
 * @ClassName: TbRuleFactory
 * @Description: 分表路由规则工厂类
 * @author Michael.小狼. email：654070332@qq.com
 * @date 2017年3月7日 上午10:23:17
 *
 */
public class TbRuleFactory implements RuleFactory{
	@Override
	public Rule getRule() {
		return new TbRule();
	}
}