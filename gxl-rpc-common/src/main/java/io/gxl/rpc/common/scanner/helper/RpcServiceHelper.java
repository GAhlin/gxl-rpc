package io.gxl.rpc.common.scanner.helper;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public class RpcServiceHelper {

    /**
     * 拼接字符串
     * @param serviceName 服务名称
     * @param serviceVersion 服务版本号
     * @param group 服务分组
     * @return 服务名称#服务版本号#服务分组
     */
    public static String buildServiceKey(String serviceName, String serviceVersion, String group) {
        return String.join("#", serviceName, serviceVersion, group);
    }
}
