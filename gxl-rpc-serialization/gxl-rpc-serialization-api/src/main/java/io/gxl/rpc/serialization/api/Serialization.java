package io.gxl.rpc.serialization.api;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public interface Serialization {

    /**
     * 序列化
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> cls);
}
