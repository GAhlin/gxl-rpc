package io.gxl.rpc.protocol;

import io.gxl.rpc.protocol.header.RpcHeader;

import java.io.Serializable;

/**
 * @author guoxiaolin
 * @version 1.0.0
 * @description Rpc协议
 */
public class RpcProtocol<T> implements Serializable {
    private static final long serialVersionUID = 292789485166173277L;

    /**
     * 消息头
     */
    private RpcHeader header;
    /**
     * 消息体
     */
    private T body;

    public RpcHeader getHeader() {
        return header;
    }

    public void setHeader(RpcHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
