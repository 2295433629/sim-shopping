package com.sim.shopping.domain.common.exception;

/**
 * Business异常
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /** 获取Code */
    public int getCode() {
        return code;
    }

}
