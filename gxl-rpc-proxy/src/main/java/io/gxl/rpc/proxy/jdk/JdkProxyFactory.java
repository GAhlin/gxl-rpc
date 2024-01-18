package io.gxl.rpc.proxy.jdk;

import io.gxl.rpc.proxy.api.BaseProxyFactory;
import io.gxl.rpc.proxy.api.ProxyFactory;
import io.gxl.rpc.proxy.api.consumer.Consumer;
import io.gxl.rpc.proxy.api.object.ObjectProxy;

import java.lang.reflect.Proxy;

/**
 * @author guoxiaolin
 * @date 2024/1/18
 * @description
 */
public class JdkProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {
    @Override
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                objectProxy
        );
    }
}
