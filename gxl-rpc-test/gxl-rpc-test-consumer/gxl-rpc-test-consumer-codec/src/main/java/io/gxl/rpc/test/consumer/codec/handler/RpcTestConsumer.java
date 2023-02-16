package io.gxl.rpc.test.consumer.codec.handler;

import io.gxl.rpc.test.consumer.codec.handler.init.RpcTestConsumerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public class RpcTestConsumer {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
        try{
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcTestConsumerInitializer());
            bootstrap.connect("127.0.0.1", 27880).sync();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            Thread.sleep(2000);
            eventLoopGroup.shutdownGracefully();
        }
    }
}
