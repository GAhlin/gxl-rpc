package io.gxl.rpc.test.consumer.codec.handler.init;

import io.gxl.rpc.codec.RpcDecoder;
import io.gxl.rpc.codec.RpcEncoder;
import io.gxl.rpc.test.consumer.codec.handler.RpcTestConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public class RpcTestConsumerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast(new RpcEncoder());
        cp.addLast(new RpcDecoder());
        cp.addLast(new RpcTestConsumerHandler());
    }
}
