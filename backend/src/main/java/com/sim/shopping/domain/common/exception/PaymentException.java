package com.sim.shopping.domain.common.exception;

public class PaymentException {

    public static class PaymentNotFoundException extends BusinessException {
        public PaymentNotFoundException(String message) {
            super(40801, message);
        }
    }

    public static class PaymentFailedException extends BusinessException {
        public PaymentFailedException(String message) {
            super(40802, message);
        }
    }

    public static class PaymentTimeoutException extends BusinessException {
        public PaymentTimeoutException(String message) {
            super(40803, message);
        }
    }

    public static class RefundFailedException extends BusinessException {
        public RefundFailedException(String message) {
            super(40804, message);
        }
    }
}
