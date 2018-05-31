package cn.sogoucloud.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.sogoucloud.framework.util.ReflectionUtil;

/**
 * bean助手类
 * @author 朱培
 *
 */
public class BeanHelper {
	
	/**
	 * 定义bean映射（用于存放bean类和bean实例的映射关系）
	 */
	private static final Map<Class<?>,Object>  BEAN_MAP=new HashMap<Class<?>,Object>();
	
	static{
		Set<Class<?>>  beanClassSet=ClassHelper.getBeanClassSet();
		for (Class<?> beanClass : beanClassSet) {
			Object object = ReflectionUtil.newInstance(beanClass);
			BEAN_MAP.put(beanClass, object);
		}
	}
	
	/**
	 * 获取Bean映射
	 */
	public static Map<Class<?>,Object> getBeanMap(){
		return BEAN_MAP;
	}
	
	/**
	 * 获取Bean实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> cls){
		if(!BEAN_MAP.containsKey(cls)){
			throw new RuntimeException("can not get Bean by class:"+cls);
		}
		return (T) BEAN_MAP.get(cls);
	}
	
	

}
