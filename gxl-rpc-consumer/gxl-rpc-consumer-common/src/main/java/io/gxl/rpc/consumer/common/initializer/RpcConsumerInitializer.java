package io.gxl.rpc.consumer.common.initializer;

import io.gxl.rpc.codec.RpcDecoder;
import io.gxl.rpc.codec.RpcEncoder;
import io.gxl.rpc.consumer.common.handler.RpcConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author guoxiaolin
 * @date 2023/2/21
 * @description
 */
public class RpcConsumerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline cp = channel.pipeline();
        cp.addLast(new RpcEncoder());
        cp.addLast(new RpcDecoder());
        cp.addLast(new RpcConsumerHandler());
    }
}
