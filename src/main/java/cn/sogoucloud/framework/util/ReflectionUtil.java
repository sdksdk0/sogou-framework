package cn.sogoucloud.framework.util;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 * @author 朱培
 *
 */
public final class ReflectionUtil {
	
	private  static final Logger logger=LoggerFactory.getLogger(ReflectionUtil.class);
	
	/**
	 * 创建实例
	 */
	public static Object newInstance(Class<?> cls){
		Object obj;
		try {
			obj=cls.newInstance();
		} catch (InstantiationException e) {
			logger.error("new instance failure",e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			logger.error("new instance failure",e);
			throw new RuntimeException(e);
		}
		return obj;
	}
	
    /**
     * 创建实例（根据类名）
     */
    public static Object newInstance(String className) {
        Class<?> cls = ClassUtil.loadClass(className);
        return newInstance(cls);
    }
    
	/**
	 * 调用方法
	 */
	public static Object invokeMethod(Object obj,Method method,Object...args ){
		 Object result;
	        try {
	        	if (args.length == 0) {
	        		args = new Object[1];
	        	}
	            method.setAccessible(true); //禁用安全检查
	            result = method.invoke(obj,args);
	        } catch (Exception e) {
	            logger.error("invoke method failure", e);
	            throw new RuntimeException(e);
	        }
	        return result;
	}
	
	/**
     * 设置成员变量的值
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            logger.error("set field failure", e);
            throw new RuntimeException(e);
        }
    }

}
