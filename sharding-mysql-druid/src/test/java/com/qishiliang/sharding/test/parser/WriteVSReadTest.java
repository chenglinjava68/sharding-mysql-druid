package com.qishiliang.sharding.test.parser;

import org.junit.Test;

import com.qishiliang.sharding.utils.ShardingUtil;

public class WriteVSReadTest {
	
	@Test
	public void parseWR(){
		System.out.println(String.valueOf(ShardingUtil.getBeginIndex(true)));
		
		System.out.println(String.valueOf(ShardingUtil.getBeginIndex(false)));
	}
}
