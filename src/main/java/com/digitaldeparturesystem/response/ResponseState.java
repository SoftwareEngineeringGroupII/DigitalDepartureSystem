package com.digitaldeparturesystem.response;

public enum ResponseState {

    SUCCESS(20000,"操作成功",true),
    LOGIN_SUCCESS(20001,"登陆成功",true),
    JOIN_SUCCESS(20001,"注册成功",true),
    FAILED(40000,"操作失败",false),
    GET_RESOURCE_FAILED(40001,"获取资源失败",false),
    ACCOUNT_NOT_LOGIN(40002,"账号未登录",false),
    PERMISSION_DENIED(40003,"无权操作",false),
    ACCOUNT_DENIED(40004,"账号被禁止",false),
    ERROR_403(40005,"权限不足",false),
    ERROR_404(40006,"页面丢失",false),
    ERROR_504(40007,"系统繁忙，请稍后重试",false),
    ERROR_505(40008,"请求错误，请检查所提交数据",false),
    LOGIN_FAILED(49999,"登录失败",false);

    ResponseState(int code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    private int code;
    private String message;
    private boolean success;

    public int getCode() {
        return code;
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
