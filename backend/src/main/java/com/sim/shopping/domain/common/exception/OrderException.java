package com.sim.shopping.domain.common.exception;

/**
 * Order异常
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class OrderException {

    public static class OrderNotFoundException extends BusinessException {
        public OrderNotFoundException(String message) {
            super(40701, message);
        }
    }

    public static class OrderStatusException extends BusinessException {
        public OrderStatusException(String message) {
            super(40702, message);
        }
    }

    public static class EmptyCartException extends BusinessException {
        public EmptyCartException(String message) {
            super(40703, message);
        }
    }

    public static class OrderCannotCancelException extends BusinessException {
        public OrderCannotCancelException(String message) {
            super(40704, message);
        }
    }
}
