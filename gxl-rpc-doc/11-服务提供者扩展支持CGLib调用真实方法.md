## 一、前言
> 我不想用反射技术调用真实方法，怎么办？

在前面的章节中，服务提供者基于Java的反射技术实现了对真实方法的调用，并对服务消费者向服务提供者发送数据、服务提供者调用真实方法、服务提供者向服务消费者响应结果数据进行了测试。

那么问题来了，服务消费者向服务提供者发送数据、服务提供者调用真实方法、服务提供者向服务消费者响应结果数据的整个请求与响应链路都是通畅的，但是目前对于服务提供者来说，美中不足的就是在调用真实方法时，使用的是Java的反射技术。如果在gxl-rpc框架中，服务提供者只能够使用Java的反射技术调用真实方法，一方面会限制了框架了扩展，一方面也不利于框架的性能优化。

那除了反射技术能够调用方法外，还有没有其他的技术也能够调用远程方法呢？答案就是使用CGLib也能够调用远程方法。

## 二、目标
> 我想在gxl-rpc框架的服务提供者中，支持CGLib调用真实方法！

都说在开发系统，尤其是在开发通用型框架时，一定要注重框架的性能和扩展性。在目前实现的gxl-rpc框架中，服务提供者只支持使用Java的反射技术调用真实方法，大家也都知道其实反射技术的性能还是稍微有点差劲的，除了性能问题外，再由就是限制了gxl-rpc框架的扩展性。

所以，在服务提供者端除了能够使用Java的反射技术调用真实方法外，也需要支持CGLib调用真实方法，用户在使用gxl-rpc框架时，在启动服务提供者时，可以根据实际需要配置使用Java反射技术调用真实方法，还是使用CGLib技术调用真实方法。

## 三、设计
> 如果让你实现服务提供者扩展支持CGLib调用真实方法，你会怎么设计？

服务提供者需要在启动的时候根据配置选择使用Java反射技术或者CGLib调用真实方法，其实就是在构建服务提供者时，再多暴露一个参数，这个参数就是选择使用Java反射技术或者CGLib调用真实方法的一个标识，整体如图11-1所示。

![img.png](img/img11-1.png)

由图11-1可以看出，当服务提供者扩展支持CgLib调用真实方法时，需要在启动服务提供者之前，通过单独的配置项配置服务提供者使用Java反射还是CGLib来调用真实方法。服务提供者在启动后，会读取对应的配置项，然后根据对应的配置项路由到使用Java反射还是CGLib来调用真实方法。

随后，真实方法执行业务逻辑，并向服务提供者返回结果数据。后续服务提供者向服务消费者响应对应的结果数据，整个过程与前面的章节一致。

## 四、实现

> 说了这么多，服务提供者扩展支持CGLib调用真实方法的代码该如何实现呢？

### 1.工程结构

- gxl-rpc-annotation：实现gxl-rpc框架的核心注解工程。
- gxl-rpc-codec：实现gxl-rpc框架的自定义编解码功能。
- gxl-rpc-common：实现gxl-rpc框架的通用工具类，包含服务提供者注解与服务消费者注解的扫描器。
- gxl-rpc-constants：存放实现gxl-rpc框架通用的常量类。
- gxl-rpc-protocol：实现gxl-rpc框架的自定义网络传输协议的工程。
- gxl-rpc-provider：服务提供者父工程。
  - gxl-rpc-provider-common：服务提供者通用工程。
  - gxl-rpc-provider-native：以纯Java方式启动gxl-rpc框架的工程。
- gxl-rpc-serialization：实现gxl-rpc框架序列化与反序列化功能的父工程。
  - gxl-rpc-serialization-api：实现gxl-rpc框架序列化与反序列化功能的通用接口工程。
  - gxl-rpc-serialization-jdk：以JDK的方式实现序列化与反序列化功能。
- gxl-rpc-test：测试gxl-rpc框架的父工程。
  - gxl-rpc-test-api：测试的通用Servcie接口工程
  - gxl-rpc-test-provider：测试服务提供者的工程。
  - gxl-rpc-test-consumer：测试服务消费者的工程
  - gxl-rpc-test-consumer-codec：测试服务消费者基于自定义网络协议与编解码与服务提供者进行数据交互
  - gxl-rpc-test-scanner：测试扫描器的工程。

### 2.核心类实现关系
本章对于核心类之间的关系没有太大的扩展和变动，大家参考第10章的核心类实现关系即可。
### 3.修改服务提供者RpcProviderHandler数据处理器
RpcProviderHandler类位于gxl-rpc-provider-common工程下的io.gxl.rpc.provider.common.handler.RpcProviderHandler，具体修改步骤如下所示。

#### （1）新增reflectType常量

在RpcProviderHandler类中新增String类型的常量reflectType，源码如下所示。
```java
//调用采用哪种类型调用真实方法
private final String reflectType;
```

#### （2）修改构造方法

修改RpcProviderHandler类的构造方法，在构造方法中新增String类型的变量reflectType，并在构造方法中将传入的reflectType变量赋值给常量reflectType，源码如下所示。

```java
public RpcProviderHandler(String reflectType, Map<String, Object> handlerMap){
    this.reflectType = reflectType;
    this.handlerMap = handlerMap;
}
```

#### （3）修改invokeMethod()方法

RpcProviderHandler类中修改前的invokeMethod()方法的源码如下所示。

```java
private Object invokeMethod(Object serviceBean, Class<?> serviceClass,String methodName, Class<?>[] parameterTypes,Object[] parameters) throws Throwable {
    Method method = serviceClass.getMethod(methodName, parameterTypes);
    method.setAccessible(true);
    return method.invoke(serviceBean, parameters);
}
```

可以看到，在RpcProviderHandler类中修改前的invokeMethod()方法中，只支持使用Java反射技术调用真实方法。

RpcProviderHandler类中修改后的invokeMethod()方法的源码如下所示。

```java
private Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
    switch (this.reflectType){
        case RpcConstants.REFLECT_TYPE_JDK:
            return this.invokeJDKMethod(serviceBean, serviceClass, methodName,parameterTypes, parameters);
        case RpcConstants.REFLECT_TYPE_CGLIB:
            return this.invokeCGLibMethod(serviceBean, serviceClass, methodName,parameterTypes, parameters);
        default:
            throw new IllegalArgumentException("not support reflect type");
    }
}
```

可以看到，在RpcProviderHandler类中修改后的invokeMethod()方法中，使用传入的reflectType作为路由条件，如果是RpcConstants.REFLECT_TYPE_JDK常量，则通过invokeJDKMethod()方法调用真实方法，如果是REFLECT_TYPE_CGLIB常量，则通过invokeCGLibMethod调用真实方法。否则直接抛出异常。

其中，RpcConstants.REFLECT_TYPE_JDK常量的值为jdk，表示使用Java反射技术调用真实方法。RpcConstants.REFLECT_TYPE_CGLIB常量的值为cglib，表示使用CGLib调用真实方法。

#### （4）新增invokeJDKMethod()方法

在RpcProviderHandler类中新增invokeJDKMethod()方法，invokeJDKMethod()方法表示使用Java反射的方式调用真实方法，源码如下所示。

```java
private Object invokeJDKMethod(Object serviceBean, Class<?> serviceClass,String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
    // JDK reflect
    logger.info("use jdk reflect type invoke method...");
    Method method = serviceClass.getMethod(methodName, parameterTypes);
    method.setAccessible(true);
    return method.invoke(serviceBean, parameters);
}
```

（5）新增invokeCGLibMethod()方法

在RpcProviderHandler类中新增invokeCGLibMethod()方法，invokeCGLibMethod()方法表示使用CGLib方式调用真实方法，源码如下所示。

```java
private Object invokeCGLibMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
    // Cglib reflect
    logger.info("use cglib reflect type invoke method...");
    FastClass serviceFastClass = FastClass.create(serviceClass);
    FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
    return serviceFastMethod.invoke(serviceBean, parameters);
}
```

### 4.修改服务提供者BaseServer基础服务类

BaseServer类位于gxl-rpc-provider-common工程下的io.gxl.rpc.provider.common.server.base.BaseServer，具体修改步骤如下所示。

#### （1）新增reflectType成员变量

在BaseServer类中新增String类型的成员变量reflectType，源码如下所示。

```java
private String reflectType;
```

#### （2）修改构造方法

修改BaseServer类的构造方法，在构造方法中新增String类型的变量reflectType，并在构造方法中将传入的reflectType变量赋值给成员变量reflectType，源码如下所示。

```java
public BaseServer(String serverAddress, String reflectType){
    if (!StringUtils.isEmpty(serverAddress)){
        String[] serverArray = serverAddress.split(":");
        this.host = serverArray[0];
        this.port = Integer.parseInt(serverArray[1]);
    }
    this.reflectType = reflectType;
}
```

#### （3）修改构建RpcProviderHandler对象的方法
在BaseServer类的startNettyServer()方法中，修改构建RpcProviderHandler对象的方法，修改后的代码片段如下所示。
```java
channel.pipeline()
    .addLast(new RpcDecoder())
    .addLast(new RpcEncoder())
    .addLast(new RpcProviderHandler(reflectType, handlerMap));
```

### 5.修改以Java原生方式启动的RpcSingleServer类
主要就是修改RpcSingleServer类的构造方法，添加String类型的reflectType参数，并将reflectType参数传递到调用父类的构造方法中，源码如下所示。
```java
public RpcSingleServer(String serverAddress, String scanPackage, String reflectType) {
    //调用父类构造方法
    super(serverAddress, reflectType);
    try {
        this.handlerMap = RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(scanPackage);
    } catch (Exception e) {
        logger.error("RPC Server init error", e);
    }
}
```
至此，完成了服务提供者支持以CGLib方式调用真实方法的扩展。

## 五、测试
> 服务提供者扩展了CGLib方式调用真实方法，不测试下怎么行？

### 1.测试Java反射调用真实方法
#### （1）修改RpcSingleServerTest类
修改gxl-rpc-test-provider工程下的io.gxl.rpc.test.provider.single.RpcSingleServerTest类，在startRpcSingleServer()方法中，调用RpcSingleServer类的构造方法创建对象时，新增reflectType参数，这里，先将reflectType参数的值设置为jdk，源码如下所示。
```java
@Test
public void startRpcSingleServer(){
    RpcSingleServer singleServer = new RpcSingleServer("127.0.0.1:27880","io.gxl.rpc.test", "jdk");
    singleServer.startNettyServer();
}
```
#### （2）启动服务提供者
启动gxl-rpc-test-provider工程下的io.gxl.rpc.test.provider.single.RpcSingleServerTest类的startRpcSingleServer()方法，输出的结果信息如下所示。
```shell
INFO BaseServer:82 - Server started on 127.0.0.1:27880
```
可以看到，服务提供者启动成功。
#### （3）启动服务消费者并发送数据

启动gxl-rpc-test-consumer-codec工程下的io.gxl.rpc.test.consumer.codec.RpcTestConsumer类中的main()方法，输出的结果信息如下所示。

```shell
17:32:43,661  INFO RpcTestConsumerHandler:24 - 发送数据开始...
17:32:43,754  INFO RpcTestConsumerHandler:38 - 服务消费者发送的数据===>>>{"body":{"async":false,"className":"io.gxl.rpc.test.api.DemoService","group":"gxl","methodName":"hello","oneway":false,"parameterTypes":["java.lang.String"],"parameters":["gxl"],"version":"1.0.0"},"header":{"magic":16,"msgLen":0,"msgType":1,"requestId":1,"serializationType":"jdk","status":1}}
17:32:43,787  INFO RpcTestConsumerHandler:40 - 发送数据完毕...
17:32:43,847  INFO RpcTestConsumerHandler:45 - 服务消费者接收到的数据===>>>{"body":{"async":false,"oneway":false,"result":"hello gxl"},"header":{"magic":16,"msgLen":202,"msgType":2,"requestId":1,"serializationType":"jdk","status":0}}
```
可以看到，服务消费者成功向服务提供者发送了数据并正确接收到服务提供者返回的结果数据。

#### （4）再次查看服务提供者输出的日志信息，如下所示。
```shell
17:32:43,824  INFO RpcProviderHandler:115 - use jdk reflect type invoke method...
17:32:43,826  INFO ProviderDemoServiceImpl:18 - 调用hello方法传入的参数为===>>>gxl
```
可以看到，服务提供者使用了Java反射的方式调用真实方法，并在真实方法中打印了传入的参数信息。

### 2.测试CGlib调用真实方法
如同JDK测试步骤

## 六、总结

Java的反射技术通常被广泛应用到通用型框架的设计与开发中，使用反射技术能够调用类中的方法，但是反射技术不是唯一能够调用类中方法的技术，CGLib也能够实现调用类中的方法。本章，我们就实现了在gxl-rpc框架的服务提供者中支持以CGLib的方式调用真实方法。

在测试阶段，直接通过调用RpcSingleServer构造方法创建RpcSingleServer类的实例对象，并通过实例对象调用RpcSingleServer类中的startNettyServer()启动服务提供者，大家也可以将创建RpcSingleServer实例对象的参数配置化。此时，如果想修改服务提供者的参数时，只需要修改配置即可。这里，我就不再赘述了。