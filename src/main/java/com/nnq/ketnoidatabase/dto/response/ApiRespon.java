package com.nnq.ketnoidatabase.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiRespon<T> {
    @Builder.Default
    private int code = 1000;

    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public ApiRespon() {}

    public ApiRespon(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
