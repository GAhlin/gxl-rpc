package io.gxl.rpc.protocol.header;

import io.gxl.rpc.common.scanner.id.IdFactory;
import io.gxl.rpc.constants.RpcConstants;
import io.gxl.rpc.protocol.enumeration.RpcType;

public class RpcHeaderFactory {

    public static RpcHeader getRpcRequestHeader(String serializationType) {
        RpcHeader rpcHeader = new RpcHeader();
        long requestId = IdFactory.getId();
        rpcHeader.setRequestId(requestId);
        rpcHeader.setMagic(RpcConstants.MAGIC);
        rpcHeader.setRequestId(requestId);
        rpcHeader.setMsgType((byte) RpcType.REQUEST.getType());
        rpcHeader.setStatus((byte) 0x1);
        rpcHeader.setSerializationType(serializationType);
        return rpcHeader;
    }
}
