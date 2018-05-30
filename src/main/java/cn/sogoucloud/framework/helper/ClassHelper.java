package cn.sogoucloud.framework.helper;

import java.util.HashSet;
import java.util.Set;

import cn.sogoucloud.framework.annotation.Controller;
import cn.sogoucloud.framework.annotation.Service;
import cn.sogoucloud.framework.util.ClassUtil;

public final class ClassHelper {
	/**
	 * 定义类集合，用于存放所需要加载的类
	 */
	private static final Set<Class<?>> CLASS_SET;
	
	static{
		String basePackage = ConfigHelper.getAppBasePackage();
		CLASS_SET=ClassUtil.getClassSet(basePackage);
	}
	
	/**
	 * 获取应用包名下的所有类
	 */
	
	public static Set<Class<?>> getClassSet(){
		return CLASS_SET;
	}
	
	/*
	 * 获取应用包下所有的Service类
	 */
	
	public static Set<Class<?>>  getServiceClassSet(){
		Set<Class<?>>  classSET = new HashSet<Class<?>>();
		for (Class<?> cls : CLASS_SET) {
			if(cls.isAnnotationPresent(Service.class)){
				classSET.add(cls);
			}
		}
		return classSET;
	}
	
	/*
	 * 获取应用包下所有的Controller类
	 */
	
	public static Set<Class<?>>  getControllerClassSet(){
		Set<Class<?>>  classSET = new HashSet<Class<?>>();
		for (Class<?> cls : CLASS_SET) {
			if(cls.isAnnotationPresent(Controller.class)){
				classSET.add(cls);
			}
		}
		return classSET;
	}
	
	/*
	 * 获取应用包下所有的Bean类
	 */
	
	public static Set<Class<?>>  getBeanClassSet(){
		Set<Class<?>>  classSET = new HashSet<Class<?>>();
		classSET.addAll(getServiceClassSet());
		classSET.addAll(getControllerClassSet());
		return classSET;
	}
	

}
