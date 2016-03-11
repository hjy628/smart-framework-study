package org.smart4j.framework.proxy;

/**
 * Created by hjy on 15-12-31.
 * 代理接口
 */
public interface Proxy {
    /**
    * 执行链式代理
    */
    Object doProxy(ProxyChain proxyChain) throws Throwable;


}
