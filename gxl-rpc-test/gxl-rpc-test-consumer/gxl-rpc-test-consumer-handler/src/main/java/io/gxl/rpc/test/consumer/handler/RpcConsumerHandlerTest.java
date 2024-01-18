package io.gxl.rpc.test.consumer.handler;

import io.gxl.rpc.consumer.common.RpcConsumer;
import io.gxl.rpc.consumer.common.callback.AsyncRPCCallback;
import io.gxl.rpc.consumer.common.future.RPCFuture;
import io.gxl.rpc.protocol.RpcProtocol;
import io.gxl.rpc.protocol.header.RpcHeaderFactory;
import io.gxl.rpc.protocol.request.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guoxiaolin
 * @date 2023/2/21
 * @description
 */
public class RpcConsumerHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(RpcConsumerHandlerTest.class);

    public static void main(String[] args) throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        RPCFuture rpcFuture = consumer.sendRequest(getRpcRequestProtocol());
        rpcFuture.addCallback(new AsyncRPCCallback() {
            @Override
            public void onSuccess(Object result) {
                logger.info("从服务消费者获取到的数据===>>>" + result);
            }
            @Override
            public void onException(Exception e) {
                logger.info("抛出了异常===>>>" + e);
            }
        });
        Thread.sleep(200);
        consumer.close();
    }

    private static RpcProtocol<RpcRequest> getRpcRequestProtocol(){
        //模拟发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<RpcRequest>();
        protocol.setHeader(RpcHeaderFactory.getRpcRequestHeader("jdk"));
        RpcRequest request = new RpcRequest();
        request.setClassName("io.gxl.rpc.test.api.DemoService");
        request.setGroup("gxl");
        request.setMethodName("hello");
        request.setParameters(new Object[]{"gxl"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(false);
        request.setOneway(false);
        protocol.setBody(request);
        return protocol;
    }
}
