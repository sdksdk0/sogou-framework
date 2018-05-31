package cn.sogoucloud.framework.proxy;

/**
 * 代理接口
 * @author 朱培
 *
 */
public interface Proxy {
	
	/**
	 * 执行链式代理
	 */
	Object doProxy(ProxyChain proxyChain) throws Throwable;

}
