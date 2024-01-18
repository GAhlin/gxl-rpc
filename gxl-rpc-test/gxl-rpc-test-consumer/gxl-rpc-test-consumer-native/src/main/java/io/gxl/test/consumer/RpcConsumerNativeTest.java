package io.gxl.test.consumer;

import io.gxl.rpc.consumer.RpcClient;
import io.gxl.rpc.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guoxiaolin
 * @date 2024/1/18
 * @description
 */
public class RpcConsumerNativeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerNativeTest.class);

    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient("1.0.0", "gxl", "jdk", 3000, false, false);
        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("gxl");
        LOGGER.info("返回的结果数据===>>> " + result);
        rpcClient.shutdown();
    }
}
