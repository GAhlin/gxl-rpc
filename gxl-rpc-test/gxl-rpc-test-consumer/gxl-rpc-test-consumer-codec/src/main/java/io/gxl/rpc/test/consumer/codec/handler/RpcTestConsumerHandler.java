package io.gxl.rpc.test.consumer.codec.handler;

import com.alibaba.fastjson.JSONObject;
import io.gxl.rpc.protocol.RpcProtocol;
import io.gxl.rpc.protocol.header.RpcHeaderFactory;
import io.gxl.rpc.protocol.request.RpcRequest;
import io.gxl.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public class RpcTestConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private final Logger logger = LoggerFactory.getLogger(RpcTestConsumerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("发送数据开始...");
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
        logger.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
        ctx.writeAndFlush(protocol);
        logger.info("发送数据完毕...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol) throws Exception {
        logger.info("服务消费者接收到的数据===>>>{}", JSONObject.toJSONString(protocol));
    }
}
