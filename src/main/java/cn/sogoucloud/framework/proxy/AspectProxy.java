package cn.sogoucloud.framework.proxy;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 切面代理
 * @author 
 *
 */
public abstract class AspectProxy implements Proxy{
	
	private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);
	
	@Override
	public final Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result = null;
		Class<?> targetClass = proxyChain.getTargetClass();
		Method targetMethod = proxyChain.getTargetMethod();
		Object[] methodParams = proxyChain.getMethodParams();
		
		begin();
		
		try {
			if(intercept(targetClass,targetMethod,methodParams)){
				before(targetClass,targetMethod,methodParams);
				result = proxyChain.doProxyChain();
				after(targetClass,targetMethod,methodParams);
			}else{
				result = proxyChain.doProxyChain();
			}
		} catch (Exception e) {
			logger.error("proxy failure",e);
			error(targetClass,targetMethod,methodParams);
			throw e;
		}finally{
			end();
		}
		
		
		return null;
	}

	private void end() {
		
		
	}

	private void error(Class<?> targetClass, Method targetMethod, Object[] methodParams) {
		
		
	}

	private void after(Class<?> targetClass, Method targetMethod, Object[] methodParams) {
	
		
	}

	private void before(Class<?> targetClass, Method targetMethod, Object[] methodParams) {
	
		
	}

	private boolean intercept(Class<?> targetClass, Method targetMethod, Object[] methodParams) {
	
		return true;
	}

	private void begin() {
	
		
	}
			

}
