package com.sim.shopping.domain.common.exception;

public class MerchantException {

    public static class MerchantNotFoundException extends BusinessException {
        public MerchantNotFoundException(String message) {
            super(40501, message);
        }
    }

    public static class MerchantAlreadyExistsException extends BusinessException {
        public MerchantAlreadyExistsException(String message) {
            super(40502, message);
        }
    }

    public static class MerchantDisabledException extends BusinessException {
        public MerchantDisabledException(String message) {
            super(40503, message);
        }
    }

    public static class ShopNotFoundException extends BusinessException {
        public ShopNotFoundException(String message) {
            super(40504, message);
        }
    }
}
