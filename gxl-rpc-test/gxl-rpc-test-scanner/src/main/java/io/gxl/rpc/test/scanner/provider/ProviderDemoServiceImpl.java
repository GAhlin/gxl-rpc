package io.gxl.rpc.test.scanner.provider;

import io.gxl.rpc.annotation.RpcService;
import io.gxl.rpc.test.scanner.service.DemoService;

/**
 * @author guoxiaolin
 * @date 2023/2/14
 * @description DemoService实现类
 */
@RpcService(interfaceClass = DemoService.class, interfaceClassName = "io.gxl.rpc.test.scanner.service.DemoService", version = "1.0.0", group = "gxl")
public class ProviderDemoServiceImpl implements DemoService {
}
