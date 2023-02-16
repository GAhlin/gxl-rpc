package io.gxl.rpc.test.provider.single;


import io.gxl.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public class RpcSingleServerTest {

    @Test
    public void startRpcSingleServer(){
        RpcSingleServer singleServer = new RpcSingleServer("127.0.0.1:27880", "io.gxl.rpc.test", "jdk");
        singleServer.startNettyServer();
    }
}
