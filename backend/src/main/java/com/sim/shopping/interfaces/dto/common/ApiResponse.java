package com.sim.shopping.interfaces.dto.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;
    private String timestamp;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private ApiResponse() {
        this.timestamp = LocalDateTime.now().format(FORMATTER);
    }

    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        return response;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<T> error(int code, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public int getCode() { return this.code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return this.message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return this.data; }
    public void setData(T data) { this.data = data; }
    public String getTimestamp() { return this.timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
