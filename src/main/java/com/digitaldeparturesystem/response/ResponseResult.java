package com.digitaldeparturesystem.response;

import java.io.Serializable;

public class ResponseResult implements Serializable {

    private boolean success;
    private int code;
    private String message;
    private Object data;


    public ResponseResult() {
    }

    public ResponseResult(ResponseStatus responseState) {
        this.success = responseState.isSuccess();
        this.code = responseState.getCode();
        this.message = responseState.getMessage();
    }

    public static ResponseResult GET(ResponseStatus state){
        return new ResponseResult(state);
    }

    public static ResponseResult SUCCESS(){
        return new ResponseResult(ResponseStatus.SUCCESS);
    }

    public static ResponseResult ACCOUNT_NOT_LOGIN(){
        return new ResponseResult(ResponseStatus.ACCOUNT_NOT_LOGIN);
    }

    public static ResponseResult PERMISSION_DENIED(){
        return new ResponseResult(ResponseStatus.PERMISSION_DENIED);
    }

    public static ResponseResult ACCOUNT_DENIED(){
        return new ResponseResult(ResponseStatus.ACCOUNT_DENIED);
    }

    public static ResponseResult LOGIN_FAILED(){
        return new ResponseResult(ResponseStatus.LOGIN_FAILED);
    }

    public static ResponseResult LOGOUT_SUCCESS() {
        return new ResponseResult(ResponseStatus.LOGOUT_SUCCESS);
    }

    public static ResponseResult ERROR_403(){
        return new ResponseResult(ResponseStatus.ERROR_403);
    }

    public static  ResponseResult ERROR_404(){
        return new  ResponseResult(ResponseStatus.ERROR_404);
    }

    public static  ResponseResult ERROR_504(){
        return new  ResponseResult(ResponseStatus.ERROR_504);
    }

    public static  ResponseResult ERROR_505(){
        return new  ResponseResult(ResponseStatus.ERROR_505);
    }

    public static  ResponseResult SUCCESS(String message){
         ResponseResult responseResult = new  ResponseResult(ResponseStatus.SUCCESS);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static  ResponseResult FAILED(){
        return new  ResponseResult(ResponseStatus.FAILED);
    }

    public static  ResponseResult FAILED(String message){
         ResponseResult responseResult = new  ResponseResult(ResponseStatus.FAILED);
        responseResult.setMessage(message);
        return responseResult;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public Object getData() {
        return data;
    }

    public  ResponseResult setData(Object data) {
        this.data = data;
        return this;
    }
}
