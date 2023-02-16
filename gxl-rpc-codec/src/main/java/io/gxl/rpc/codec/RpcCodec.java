package io.gxl.rpc.codec;

import io.gxl.rpc.serialization.api.Serialization;
import io.gxl.rpc.serialization.jdk.JdkSerialization;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description 数据的编解码接口，提供一个获取序列化对象的默认方法。
 */
public interface RpcCodec {

    /**
     * 获取JdkSerialization对象
     * @return JdkSerialization
     */
    default Serialization getJdkSerialization(){
        return new JdkSerialization();
    }
}
