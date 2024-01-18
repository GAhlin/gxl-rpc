package io.gxl.rpc.proxy.api.async;

import io.gxl.rpc.proxy.api.future.RPCFuture;

/**
 * @author guoxiaolin
 * @date 2024/1/18
 * @description
 */
public interface IAsyncObjectProxy {
    /**
     * 异步代理对象调用方法
     * @param funcName 方法名称
     * @param args 方法参数
     * @return 封装好的RPCFuture对象
     */
    RPCFuture call(String funcName, Object... args);
}