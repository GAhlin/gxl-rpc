package io.gxl.rpc.test.scanner;

import io.gxl.rpc.common.scanner.ClassScanner;
import io.gxl.rpc.common.scanner.reference.RpcReferenceScanner;
import io.gxl.rpc.common.scanner.server.RpcServiceScanner;
import org.junit.Test;

import java.util.List;

/**
 * @author guoxiaolin
 * @date 2023/2/14
 * @description
 */
public class ScannerTest {

    /**
     * 扫描io.gxl.rpc.test.scanner包下所有的类
     */
    @Test
    public void testScannerClassNameList() throws Exception {
        List<String> classNameList = ClassScanner.getClassNameList("io.gxl.rpc.test.scanner");
        classNameList.forEach(System.out::println);
    }

    /**
     * 扫描io.gxl.rpc.test.scanner包下所有标注了@RpcService注解的类
     */
    @Test
    public void testScannerClassNameListByRpcService() throws Exception {
        RpcServiceScanner.
                doScannerWithRpcServiceAnnotationFilterAndRegistryService("io.gxl.rpc.test.scanner");
    }

    /**
     * 扫描io.gxl.rpc.test.scanner包下所有标注了@RpcReference注解的类
     */
    @Test
    public void testScannerClassNameListByRpcReference() throws Exception {
        RpcReferenceScanner.
                doScannerWithRpcReferenceAnnotationFilter("io.gxl.rpc.test.scanner");
    }
}
