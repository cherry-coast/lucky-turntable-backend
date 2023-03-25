package com.cherry.lucky.common.exception;

import com.cherry.lucky.domain.CherryResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : ganxiongwen
 * Date: 2022/4/20 16:30
 * Description: 全局异常
 * ClassName: GlobalException
 * Package: com.cherry.flow.common.exception
 * Copyright (c) 2022,All Rights Reserved.
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = CherryException.class)
    public CherryResponseEntity<String> handleCherryException(CherryException e) {
        log.error("module error !!! error message: {}", e.getMessage());
        return CherryResponseEntity.fail(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CherryResponseEntity<String> handleValidException(MethodArgumentNotValidException e) {
        return getFormatException(e);
    }

    @ExceptionHandler(value = BindException.class)
    public CherryResponseEntity<String> handleValidException(BindException e) {
        return getFormatException(e);
    }

    @ExceptionHandler(value = Exception.class)
    public CherryResponseEntity<String> handleValidException(Exception e) {
        log.error(e.toString());
        return CherryResponseEntity.fail(e.getMessage());
    }

    public CherryResponseEntity<String> getFormatException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        log.error(message);
        return CherryResponseEntity.fail(message);
    }
}