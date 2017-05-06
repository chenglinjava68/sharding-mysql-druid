package com.qishiliang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Normal {
	private String ss="ddd";
	private void ga(int i){
		System.out.println("ga!!"+i);
	}

}



public class Privateclassaccess {
	public static void main(String[]args) throws ClassNotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException{
		Field field=Class.forName("com.qishiliang.reflect.Normal").getDeclaredField("ss");
		field.setAccessible(true);
		Normal n=new Normal();
		System.out.println(field.getType().toString());
		System.out.println(field.getName());
		System.out.println(field.getModifiers());
		Object s=field.get(n);
		System.out.println(s);
		String x="omg";
		field.set(n, x);
		System.out.println(field.get(n));
		Method method=Class.forName("com.qishiliang.reflect.Normal").getDeclaredMethod("ga", new Class[]{int.class});
		method.setAccessible(true);
		method.invoke(n, 3);
	}

}