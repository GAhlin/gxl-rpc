package io.gxl.rpc.proxy.api.consumer;

import io.gxl.rpc.protocol.RpcProtocol;
import io.gxl.rpc.protocol.request.RpcRequest;
import io.gxl.rpc.proxy.api.future.RPCFuture;

/**
 * @author guoxiaolin
 * @date 2024/1/18
 * @description
 */
public interface Consumer {
    /**
     * 消费者发送 request 请求
     */
    RPCFuture sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception;
}
