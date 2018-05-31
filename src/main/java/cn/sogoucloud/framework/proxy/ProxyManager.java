package cn.sogoucloud.framework.proxy;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.core.MethodInfoTransformer;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代码管理器
 * @author 
 *
 */
public class ProxyManager {
	
	
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(final Class<?>  targetClass,final List<Proxy> proxyList){
		return (T) Enhancer.create(targetClass, new MethodInterceptor() {
			
			@Override
			public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
				return new ProxyChain(targetClass,targetObject,targetMethod,methodProxy,
						methodParams,proxyList).doProxyChain();
			}
		});
		
	}

}
