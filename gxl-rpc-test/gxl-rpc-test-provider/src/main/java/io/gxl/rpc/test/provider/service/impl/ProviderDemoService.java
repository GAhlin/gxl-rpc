package io.gxl.rpc.test.provider.service.impl;

import io.gxl.rpc.annotation.RpcService;
import io.gxl.rpc.test.provider.service.DemoService;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
@RpcService(interfaceClass = DemoService.class, interfaceClassName = "io.gxl.rpc.test.scanner.service.DemoService", version = "1.0.0", group = "gxl")
public class ProviderDemoService implements DemoService {
}
