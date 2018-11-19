package com.cff.jpamapper.core.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

public class ReflectUtil {

	/**
	 * 判断是不是基础类
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isGeneralClass(Class<?> type) {
		if (type.isInterface() || type.isArray() || type.isEnum() || isCollection(type) || isJavaClass(type)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是不是集合的实现类
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isCollection(Class<?> type) {
		return Collection.class.isAssignableFrom(type);
	}
	
	/**
	 * 判断是不是Iterable的实现类
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isIterable(Class<?> type) {
		return Iterable.class.isAssignableFrom(type);
	}

	/**
	 * 是不是java基础类
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isJavaClass(Class<?> type) {
		boolean isBaseClass = false;
		if (type.isArray()) {
			isBaseClass = false;
		} else if (type.isPrimitive() || type.getPackage() == null || "java.lang".equals(type.getPackage().getName())
				|| "java.math".equals(type.getPackage().getName()) || "java.sql".equals(type.getPackage().getName())
				|| "java.util".equals(type.getPackage().getName())) {
			isBaseClass = true;
		}
		return isBaseClass;
	}

	public static Class<?> getFirstParameterType(Method method) {
		Class<?> type[] = method.getParameterTypes();
		if (type == null || type.length < 1)
			return null;
		return type[0];
	}
	
	/**
	 * 检测类型是否相符
	 * @param src 子类
	 * @param dst 父类
	 * @return true类型相符
	 */
	public static boolean checkTypeFit(Class<?> src, Class<?> dst){
		if(dst.isAssignableFrom(src))return true;
		return false;
	}
	
	/**
	 * 获取泛型类型
	 * @param type
	 * @return
	 */
	public static Class<?> findGenericClass(Class<?> type){
		Type[] types = type.getGenericInterfaces();
		Class<?> entityClass = null;
		for (Type item : types) {
			if (item instanceof ParameterizedType) {
				ParameterizedType t = (ParameterizedType) item;
				Type[] ts = t.getActualTypeArguments();
				Class<?> tmpType = (Class<?>) ts[0];
				if (ReflectUtil.isGeneralClass(tmpType)) {
					entityClass = tmpType;
				}
			}
		}
		
		return entityClass;
	}
}
