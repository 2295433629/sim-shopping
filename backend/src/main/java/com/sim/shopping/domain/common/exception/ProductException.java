package com.sim.shopping.domain.common.exception;

public class ProductException {

    public static class ProductNotFoundException extends BusinessException {
        public ProductNotFoundException(String message) {
            super(40601, message);
        }
    }

    public static class ProductOffShelfException extends BusinessException {
        public ProductOffShelfException(String message) {
            super(40602, message);
        }
    }

    public static class StockNotEnoughException extends BusinessException {
        public StockNotEnoughException(String message) {
            super(40603, message);
        }
    }

    public static class SkuNotFoundException extends BusinessException {
        public SkuNotFoundException(String message) {
            super(40604, message);
        }
    }

    public static class CategoryNotFoundException extends BusinessException {
        public CategoryNotFoundException(String message) {
            super(40605, message);
        }
    }
}
