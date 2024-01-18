package io.gxl.rpc.proxy.api;

import io.gxl.rpc.proxy.api.config.ProxyConfig;
import io.gxl.rpc.proxy.api.object.ObjectProxy;

public abstract class BaseProxyFactory<T> implements ProxyFactory {
    protected ObjectProxy<T> objectProxy;
    @Override
    public <T> void init(ProxyConfig<T> proxyConfig) {
        this.objectProxy = new ObjectProxy(proxyConfig.getClazz(),
                proxyConfig.getServiceVersion(),
                proxyConfig.getServiceGroup(),
                proxyConfig.getSerializationType(),
                proxyConfig.getTimeout(),
                proxyConfig.getConsumer(),
                proxyConfig.isAsync(),
                proxyConfig.isOneway());
    }
}