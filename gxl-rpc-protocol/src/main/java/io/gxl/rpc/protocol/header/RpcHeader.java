package io.gxl.rpc.protocol.header;

import java.io.Serializable;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public class RpcHeader implements Serializable {

    private static final long serialVersionUID = 6011436680686290298L;
    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 报文类型 1byte | 状态 1byte | 消息 ID 8byte    |
    +---------------------------------------------------------------+
    |           序列化类型 16byte      | 数据长度 4byte    |
    +---------------------------------------------------------------+
    */
    /**
     * 魔数 2byte
     */
    private short magic;

    /**
     * 报文类型 1byte
     */
    private byte msgType;

    /**
     * 状态 1byte
     */
    private byte status;

    /**
     * 消息 ID 8byte
     */
    private long requestId;

    /**
     * 序列化类型16byte，不足16byte后面补0，约定序列化类型长度最多不能超过16
     */
    private String serializationType;

    /**
     * 消息长度 4byte
     */
    private int msgLen;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public int getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }
}
