package io.gxl.rpc.protocol.enumeration;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public enum RpcStatus {
    SUCCESS(0),
    FAIL(1);
    private final int code;
    RpcStatus(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}