package com.qishiliang.sharding.test.parser;

import org.junit.Test;

import com.qishiliang.sharding.parser.ResolveRouteValue;

public class ResolveRouteTest {

	@Test
	public void getRoutebySelect() {
		final String SQL1 = "SELECT * FROM user.userinfo_test WHERE uid = 1002120 and name=?";
		System.out.println(ResolveRouteValue.getRoute(SQL1).toString());
		final String SQL2 = "INSERT INTO userinfo_test(uid,name) VALUES(1002120,?)";
		System.out.println(ResolveRouteValue.getRoute(SQL2).toString());
		final String SQL3 = "UPDATE userinfo_test SET sex = ? WHERE uid=1002120 AND email=?";
		System.out.println(ResolveRouteValue.getRoute(SQL3).toString());
		final String SQL4 = "DELETE FROM userinfo_test WHERE uid=1002120 AND name=?";
		System.out.println(ResolveRouteValue.getRoute(SQL4).toString());
	}
}