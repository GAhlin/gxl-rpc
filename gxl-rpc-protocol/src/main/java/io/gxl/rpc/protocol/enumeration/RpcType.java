package io.gxl.rpc.protocol.enumeration;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public enum RpcType {
    //请求消息
    REQUEST(1),
    //响应消息
    RESPONSE(2),
    //心跳数据
    HEARTBEAT(3);

    private final int type;

    RpcType(int type) {
        this.type = type;
    }

    public static RpcType findByType(int type) {
        for (RpcType rpcType : RpcType.values()) {
            if (rpcType.getType() == type) {
                return rpcType;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }
}
