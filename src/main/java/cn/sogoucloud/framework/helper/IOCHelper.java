package cn.sogoucloud.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import cn.sogoucloud.framework.annotation.Autowired;
import cn.sogoucloud.framework.util.ArrayUtil;
import cn.sogoucloud.framework.util.CollectionUtil;
import cn.sogoucloud.framework.util.ReflectionUtil;

/**
 * 依赖注入助手类
 * @author 朱培
 *
 */
public class IOCHelper {
	
	static{
		//获取所有的Bean类与Bean实例之间的映射关系
		Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
		if(CollectionUtil.isNotEmpty(beanMap)){
			//遍历 Bean Map
			for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
				//从BeanMap中获取Bean类与Bean实例
				Class<?> beanClass = beanEntry.getKey();
				Object beanInstance = beanEntry.getValue();
				//获取Bean类定义的所有成员变量（Bean Field）
				Field[]  beanFileds=beanClass.getDeclaredFields();
				if(ArrayUtil.isNotEmpty(beanFileds)){
					for (Field field : beanFileds) {
						//判断当前的Bean Field是否带有AutoWired注解
						if(field.isAnnotationPresent(Autowired.class)){
							//在Bean Map中获取Bean Field对应的实例
							Class<?> beanFieldClass = field.getType();
							Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null) {
                                ReflectionUtil.setField(beanInstance, field, beanFieldInstance);
                            }
						}
					}
				}
			}
		}
	}
}
