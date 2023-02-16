package io.gxl.rpc.protocol.response;

import io.gxl.rpc.protocol.base.RpcMessage;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public class RpcResponse extends RpcMessage {

    private static final long serialVersionUID = 425335064405584525L;
    private String error;

    private Object result;

    public boolean isError() {
        return error != null;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

