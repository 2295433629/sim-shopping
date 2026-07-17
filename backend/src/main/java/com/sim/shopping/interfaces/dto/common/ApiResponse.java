package com.sim.shopping.interfaces.dto.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一API响应DTO，封装接口返回的code、message、data和timestamp
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;
    private String timestamp;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private ApiResponse() {
        this.timestamp = LocalDateTime.now().format(FORMATTER);
    }

    /**
     * success
     * @return 返回结果
     */
    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        return response;
    }

    /**
     * success
     * @param data data
     * @return 返回结果
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    /**
     * success
     * @param message message
     * @param data data
     * @return 返回结果
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /**
     * error
     * @param code code
     * @param message message
     * @return 返回结果
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    /**
     * error
     * @param code code
     * @param message message
     * @param data data
     * @return 返回结果
     */
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /** 获取Code */
    public int getCode() { return this.code; }
    /** set Code */
    public void setCode(int code) { this.code = code; }
    /**
     * 获取Message
     * @return 返回结果
     */
    public String getMessage() { return this.message; }
    /**
     * set Message
     * @param message message
     */
    public void setMessage(String message) { this.message = message; }
    /**
     * 获取Data
     * @return 返回结果
     */
    public T getData() { return this.data; }
    /**
     * set Data
     * @param data data
     */
    public void setData(T data) { this.data = data; }
    /**
     * 获取Timestamp
     * @return 返回结果
     */
    public String getTimestamp() { return this.timestamp; }
    /**
     * set Timestamp
     * @param timestamp timestamp
     */
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
