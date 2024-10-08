package io.gxl.rpc.protocol.base;

import java.io.Serializable;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public class RpcMessage implements Serializable {

    /**
     * 是否单向发送
     */
    private boolean oneway;

    /**
     * 是否异步调用
     */
    private boolean async;

    public boolean getOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public boolean getAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}