package io.gxl.rpc.common.scanner.exception;

/**
 * @author guoxiaolin
 * @date 2023/2/16
 * @description
 */
public class SerializerException extends RuntimeException {

    private static final long serialVersionUID = -6783134254669118520L;
    public SerializerException(final Throwable e) {
        super(e);
    }
    public SerializerException(final String message) {
        super(message);
    }
    public SerializerException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
