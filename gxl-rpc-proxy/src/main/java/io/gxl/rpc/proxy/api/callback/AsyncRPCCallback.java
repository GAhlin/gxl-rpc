package io.gxl.rpc.proxy.api.callback;

/**
 * @author guoxiaolin
 * @date 2024/1/18
 * @description
 */
public interface AsyncRPCCallback {

    /**
     * 成功后的回调方法
     */
    void onSuccess(Object result);
    /**
     * 异常的回调方法
     */
    void onException(Exception e);
}
