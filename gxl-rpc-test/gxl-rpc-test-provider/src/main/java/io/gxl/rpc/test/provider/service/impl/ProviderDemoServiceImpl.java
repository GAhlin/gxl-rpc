package io.gxl.rpc.test.provider.service.impl;

import io.gxl.rpc.annotation.RpcService;
import io.gxl.rpc.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
@RpcService(interfaceClass = DemoService.class, interfaceClassName = "io.gxl.rpc.test.scanner.service.DemoService", version = "1.0.0", group = "gxl")
public class ProviderDemoServiceImpl implements DemoService {
    private final Logger logger = LoggerFactory.getLogger(ProviderDemoServiceImpl.class);
    @Override
    public String hello(String name) {
        logger.info("调用hello方法传入的参数为===>>>{}", name);
        return "hello " + name;
    }
}
