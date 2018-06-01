package cn.sogoucloud.framework.proxy;

import java.lang.reflect.Method;

import javax.activation.DataHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sogoucloud.framework.annotation.Transaction;
import cn.sogoucloud.framework.helper.DatabaseHelper;

/**
 * 事务代理
 * @author 朱培
 *
 */
public class TransactionProxy implements Proxy{

	private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);
	
	private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
		@Override
		protected Boolean initialValue(){
			return false;
		}
	};
	
	@Override
	public Object doProxy(ProxyChain proxyChain) throws Throwable {
		Object result;
		boolean flag = FLAG_HOLDER.get();
		Method method = proxyChain.getTargetMethod();
		if(!flag && method.isAnnotationPresent(Transaction.class)){
			FLAG_HOLDER.set(true);
			try {
				DatabaseHelper.beginTransaction();
				logger.debug("begin transaction");
				result = proxyChain.doProxyChain();
				DatabaseHelper.commitTransaction();
				logger.debug("commit transaction");
			} catch (Exception e) {
				DatabaseHelper.rollbackTransaction();
				logger.debug("rollback transaction");
				throw e;
			}finally{
				FLAG_HOLDER.remove();
			}
		}else{
			result = proxyChain.doProxyChain();
		}
		return result;
	}
	
	

}
