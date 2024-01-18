package io.gxl.rpc.proxy.api.future;
import io.gxl.rpc.common.scanner.threadpool.ClientThreadPool;
import io.gxl.rpc.protocol.RpcProtocol;
import io.gxl.rpc.protocol.request.RpcRequest;
import io.gxl.rpc.protocol.response.RpcResponse;
import io.gxl.rpc.proxy.api.callback.AsyncRPCCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author guoxiaolin
 * @date 2023/4/18
 * @description
 */
public class RPCFuture extends CompletableFuture<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RPCFuture.class);

    /**
     * 内部类Sync的实例对象。
     */
    private Sync sync;

    /**
     * RpcRequest类型的协议对象
     */
    private RpcProtocol<RpcRequest> requestRpcProtocol;

    /**
     * RpcResponse类型的协议对象
     */
    private RpcProtocol<RpcResponse> responseRpcProtocol;

    /**
     * 存放回调接口
     */
    private List<AsyncRPCCallback> pendingCallbacks = new ArrayList<AsyncRPCCallback>();

    /**
     * 添加和执行回调方法时，进行加锁和解锁操作
     */
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 默认的超时时间
     */
    private long responseTimeThreshold = 5000;

    public RPCFuture(RpcProtocol<RpcRequest> requestRpcProtocol) {
        this.sync = new Sync();
        this.requestRpcProtocol = requestRpcProtocol;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    /**
     * 阻塞获取responseRpcProtocol协议对象中的实际结果数据
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(-1);
        if (this.responseRpcProtocol != null) {
            return this.responseRpcProtocol.getBody().getResult();
        } else {
            return null;
        }
    }

    /**
     * 用于异步执行回调方法
     * @param callback
     */
    private void runCallback(final AsyncRPCCallback callback) {
        final RpcResponse res = this.responseRpcProtocol.getBody();
        ClientThreadPool.submit(() -> {
            if (!res.isError()) {
                callback.onSuccess(res.getResult());
            } else {
                callback.onException(new RuntimeException("Response error", new Throwable(res.getError())));
            }
        });
    }

    /**
     * 用于外部服务添加回调接口实例对象到pendingCallbacks集合中
     * @param callback
     * @return
     */
    public RPCFuture addCallback(AsyncRPCCallback callback) {
        lock.lock();
        try {
            if (isDone()) {
                runCallback(callback);
            } else {
                this.pendingCallbacks.add(callback);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }

    /**
     * 用于依次执行pendingCallbacks集合中回调接口的方法
     */
    private void invokeCallbacks() {
        lock.lock();
        try {
            for (final AsyncRPCCallback callback : pendingCallbacks) {
                runCallback(callback);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 超时阻塞获取responseRpcProtocol协议对象中的实际结果数据
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (this.responseRpcProtocol != null) {
                return this.responseRpcProtocol.getBody().getResult();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Timeout exception. Request id: " + this.requestRpcProtocol.getHeader().getRequestId()
                    + ". Request class name: " + this.requestRpcProtocol.getBody().getClassName()
                    + ". Request method: " + this.requestRpcProtocol.getBody().getMethodName());
        }
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    /**
     * 当服务消费者接收到服务提供者响应的结果数据时，就会调用done()方法，并传入RpcResponse类型的协议对象，此时会唤醒阻塞的线程获取响应的结果数据
     * 当唤醒线程时，执行invokeCallbacks()方法，以便服务消费者获取到服务提供者响应的结果数据后，立刻主动执行回调方法
     * @param responseRpcProtocol
     */
    public void done(RpcProtocol<RpcResponse> responseRpcProtocol) {
        this.responseRpcProtocol = responseRpcProtocol;
        sync.release(1);
        //新增的调用invokeCallbacks()方法
        invokeCallbacks();
        // Threshold
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold) {
            LOGGER.warn("Service response time is too slow. Request id = " + responseRpcProtocol.getHeader().getRequestId() + ". Response Time = " + responseTime + "ms");
        }
    }

    static class Sync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = 1L;

        //future status
        private final int done = 1;
        private final int pending = 0;

        protected boolean tryAcquire(int acquires) {
            return getState() == done;
        }

        protected boolean tryRelease(int releases) {
            if (getState() == pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isDone() {
            getState();
            return getState() == done;
        }
    }
}
