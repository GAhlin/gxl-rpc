package io.gxl.rpc.consumer.common.context;

import io.gxl.rpc.proxy.api.future.RPCFuture;

/**
 * @author guoxiaolin
 * @date 2023/4/18
 * @description
 */
public class RpcContext {

    private RpcContext(){
    }

    /**
     * RpcContext实例
     */
    private static final RpcContext AGENT = new RpcContext();

    /**
     * 存放RPCFuture的InheritableThreadLocal
     */
    private static final InheritableThreadLocal<RPCFuture> RPC_FUTURE_INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * 获取上下文
     * @return RPC服务的上下文信息
     */
    public static RpcContext getContext(){
        return AGENT;
    }

    /**
     * 将RPCFuture保存到线程的上下文
     * @param rpcFuture
     */
    public void setRPCFuture(RPCFuture rpcFuture){
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.set(rpcFuture);
    }

    /**
     * 获取RPCFuture
     */
    public RPCFuture getRPCFuture(){
        return RPC_FUTURE_INHERITABLE_THREAD_LOCAL.get();
    }

    /**
     * 移除RPCFuture
     */
    public void removeRPCFuture(){
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.remove();
    }
}
