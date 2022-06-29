package com.example.shirodemo.common.utils;

public class Response<T> {
    private boolean result;
    private int code;
    private String msg;
    private T data;

    public Response() {
    }

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response();
        response.result = true;
        response.msg = "success";
        response.data = data;
        return response;
    }

    public static <T> Response<T> success() {

        return success((null));
    }

    public static <T> Response<T> fail(T data, String msg) {
        Response<T> response = new Response();
        response.result = false;
        response.msg = msg;
        response.data = data;
        return response;
    }

    public static Response fail(String msg) {
        return fail((Object)null, msg);
    }

    public static Response fail() {
        return fail("fail");
    }

    public boolean isResult() {
        return this.result;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}