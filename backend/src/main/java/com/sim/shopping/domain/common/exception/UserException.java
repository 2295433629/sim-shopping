package com.sim.shopping.domain.common.exception;

public class UserException {

    public static class UserNotFoundException extends BusinessException {
        public UserNotFoundException(String message) {
            super(40401, message);
        }
    }

    public static class UserAlreadyExistsException extends BusinessException {
        public UserAlreadyExistsException(String message) {
            super(40402, message);
        }
    }

    public static class UserDisabledException extends BusinessException {
        public UserDisabledException(String message) {
            super(40403, message);
        }
    }

    public static class PasswordErrorException extends BusinessException {
        public PasswordErrorException(String message) {
            super(40404, message);
        }
    }

    public static class TokenInvalidException extends BusinessException {
        public TokenInvalidException(String message) {
            super(40405, message);
        }
    }

    public static class TokenExpiredException extends BusinessException {
        public TokenExpiredException(String message) {
            super(40406, message);
        }
    }
}
