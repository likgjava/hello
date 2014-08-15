package com.likg.core.service.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.likg.auth.domain.User;

public class Hello extends Foo<User> {

	@SuppressWarnings("unchecked")
	public Class getPersistClass() {
		Class clazz = null;
		
		Class c = this.getClass();
		Type t = c.getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			clazz = (Class) ((ParameterizedType) t).getActualTypeArguments()[0];
		}
		System.out.println(clazz.getName());
		return clazz;
	}
	
	public static void main(String[] args) {
		Hello h = new Hello();
		h.getPersistClass();
	}
	
}
