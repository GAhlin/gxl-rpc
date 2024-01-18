package io.gxl.rpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.gxl.rpc.consumer.common.context.RpcContext;
import io.gxl.rpc.protocol.RpcProtocol;
import io.gxl.rpc.protocol.header.RpcHeader;
import io.gxl.rpc.protocol.request.RpcRequest;
import io.gxl.rpc.protocol.response.RpcResponse;
import io.gxl.rpc.proxy.api.future.RPCFuture;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guoxiaolin
 * @date 2023/2/21
 * @description
 */
public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    private final Logger logger = LoggerFactory.getLogger(RpcConsumerHandler.class);
    private volatile Channel channel;
    private SocketAddress remotePeer;
    private Map<Long, RPCFuture> pendingRPC = new ConcurrentHashMap<>();

    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getRemotePeer() {
        return remotePeer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol) throws Exception {
        if (protocol == null){
            return;
        }
        logger.info("服务消费者接收到的数据===>>>{}", JSONObject.toJSONString(protocol));
        RpcHeader header = protocol.getHeader();
        long requestId = header.getRequestId();
        RPCFuture rpcFuture = pendingRPC.remove(requestId);
        if (rpcFuture != null){
            rpcFuture.done(protocol);
        }
    }

    /**
     * 服务消费者向服务提供者发送请求
     */
    public RPCFuture sendRequest(RpcProtocol<RpcRequest> protocol, boolean async, boolean oneway){
        logger.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
        return oneway ? this.sendRequestOneway(protocol) : async ?
                sendRequestAsync(protocol) : this.sendRequestSync(protocol);
    }

    private RPCFuture getRpcFuture(RpcProtocol<RpcRequest> protocol) {
        RPCFuture rpcFuture = new RPCFuture(protocol);
        RpcHeader header = protocol.getHeader();
        long requestId = header.getRequestId();
        pendingRPC.put(requestId, rpcFuture);
        return rpcFuture;
    }

    /**
     * 同步调用的方法
     * @param protocol
     * @return
     */
    private RPCFuture sendRequestSync(RpcProtocol<RpcRequest> protocol) {
        RPCFuture rpcFuture = this.getRpcFuture(protocol);
        channel.writeAndFlush(protocol);
        return rpcFuture;
    }

    /**
     * 异步调用的方法
     * @param protocol
     * @return
     */
    private RPCFuture sendRequestAsync(RpcProtocol<RpcRequest> protocol) {
        RPCFuture rpcFuture = this.getRpcFuture(protocol);
        //如果是异步调用，则将RPCFuture放入RpcContext
        RpcContext.getContext().setRPCFuture(rpcFuture);
        channel.writeAndFlush(protocol);
        return null;
    }

    /**
     * 单向调用的方法
     * @param protocol
     * @return
     */
    private RPCFuture sendRequestOneway(RpcProtocol<RpcRequest> protocol) {
        channel.writeAndFlush(protocol);
        return null;
    }


    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
