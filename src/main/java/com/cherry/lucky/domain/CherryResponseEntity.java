package com.cherry.lucky.domain;

import com.cherry.lucky.constant.HttpCodeConstants;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : cherry
 * Date: 2023/3/21 11:30
 * Description: 服务返回基类
 * ClassName: CherryResponseEntity
 * Package: com.cherry.lucky.domain
 * Copyright (c) 2022,All Rights Reserved.
 */
@Data
@ToString
@SuppressWarnings(value = "unused")
public class CherryResponseEntity<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = HttpCodeConstants.SUCCESS;

    /**
     * 失败
     */
    public static final int FAIL = HttpCodeConstants.FAIL;

    private int code;

    private String msg;

    private T data;

    private boolean success;

    public static <T> CherryResponseEntity<T> success() {
        return restResult(null, SUCCESS, null, true);
    }

    public static <T> CherryResponseEntity<T> success(T data) {
        return restResult(data, SUCCESS, null, true);
    }

    public static <T> CherryResponseEntity<T> success(T data, String msg) {
        return restResult(data, SUCCESS, msg, true);
    }

    public static <T> CherryResponseEntity<T> fail() {
        return restResult(null, FAIL, null, false);
    }

    public static <T> CherryResponseEntity<T> fail(String msg) {
        return restResult(null, FAIL, msg, false);
    }

    public static <T> CherryResponseEntity<T> fail(T data) {
        return restResult(data, FAIL, null, false);
    }

    public static <T> CherryResponseEntity<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg, false);
    }

    public static <T> CherryResponseEntity<T> fail(int code, String msg) {
        return restResult(null, code, msg, false);
    }

    private static <T> CherryResponseEntity<T> restResult(T data, int code, String msg, boolean success) {
        CherryResponseEntity<T> apiResult = new CherryResponseEntity<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setSuccess(success);
        return apiResult;
    }
}