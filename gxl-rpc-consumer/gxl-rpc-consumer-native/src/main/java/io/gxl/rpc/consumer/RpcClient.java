package io.gxl.rpc.consumer;

import io.gxl.rpc.consumer.common.RpcConsumer;
import io.gxl.rpc.proxy.jdk.JdkProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guoxiaolin
 * @date 2024/1/18
 * @description
 */
public class RpcClient {
    private final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    /**
     * 服务版本
     */
    private String serviceVersion;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 序列化类型
     */
    private String serializationType;
    /**
     * 超时时间
     */
    private long timeout;
    /**
     * 是否异步调用
     */
    private boolean async;
    /**
     * 是否单向调用
     */
    private boolean oneway;
    public RpcClient(String serviceVersion, String serviceGroup, String serializationType, long timeout, boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.serviceGroup = serviceGroup;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }
    public <T> T create(Class<T> interfaceClass) {
        JdkProxyFactory<T> jdkProxyFactory = new JdkProxyFactory<T>(serviceVersion, serviceGroup, serializationType, timeout, RpcConsumer.getInstance(), async, oneway);
        return jdkProxyFactory.getProxy(interfaceClass);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();
    }
}
