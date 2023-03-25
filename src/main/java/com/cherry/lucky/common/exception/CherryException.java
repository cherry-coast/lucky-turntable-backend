package com.cherry.lucky.common.exception;


import com.cherry.lucky.utils.CherryStringUtil;

import java.io.Serial;

/**
 * @author : ganxiongwen
 * Date: 2022/4/20 16:30
 * Description: 异常信息
 * ClassName: CherryException
 * Package: com.cherry.flow.common.exception
 * Copyright (c) 2022,All Rights Reserved.
 */
@SuppressWarnings(value = "unused")
public class CherryException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误码对应的参数
     */
    private final Object[] args;

    /**
     * 错误消息
     */
    private final String errorMessage;

    public CherryException(int code, Object[] args, String errorMessage) {
        this.code = code;
        this.args = args;
        this.errorMessage = errorMessage;
    }

    public CherryException(int code, Object[] args) {
        this(code, args, null);
    }

    public CherryException(int code, String errorMessage) {
        this(code, null, errorMessage);
    }


    @Override
    public String getMessage() {
        String message = null;
        if (CherryStringUtil.isNotEmpty(errorMessage)) {
            message = errorMessage;
        }
        return message;
    }


    public int getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
