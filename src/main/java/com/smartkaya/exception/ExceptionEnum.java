package com.smartkaya.exception;

public enum ExceptionEnum {
    SUCCESS(200),
    PROCESSING(202),
    RESOURCE_NOT_FOUND(404),
    ARGUMENTS_INVALID(401),
    BUSINESS_ERROR(400),
    UNPROCESSABLE_ENTITY_ERROR(422),
    SERVER_ERROR(500);

    private ExceptionEnum(int code) {
        this.code = code;
    }

    private ExceptionEnum() {
    }

    // 成员变量
    private int code;

    public int getCode() {
        return this.code;
    }
}
