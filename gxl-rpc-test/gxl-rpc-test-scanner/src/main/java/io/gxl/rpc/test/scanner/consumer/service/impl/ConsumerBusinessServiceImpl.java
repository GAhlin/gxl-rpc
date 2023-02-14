package io.gxl.rpc.test.scanner.consumer.service.impl;

import io.gxl.rpc.annotation.RpcReference;
import io.gxl.rpc.test.scanner.consumer.service.ConsumerBusinessService;
import io.gxl.rpc.test.scanner.service.DemoService;

/**
 * @author guoxiaolin
 * @date 2023/2/14
 * @description 服务消费者业务逻辑实现类
 */
public class ConsumerBusinessServiceImpl implements ConsumerBusinessService {

    @RpcReference(registryType = "zookeeper", registryAddress = "127.0.0.1:2181", version = "1.0.0", group = "gxl")
    private DemoService demoService;
}
