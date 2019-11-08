package com.skyguard.teraka.entity;

public enum  RequestType {

    PRODUCER(1,"生产者"),CONSUMER(2,"消费者"),NODE(3,"节点信息")


    ;

    private int code;
    private String message;

    RequestType(int code, String message) {
        this.code = code;
        this.message = message;
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
}
