package com.edissone.bymtask.context.exception;

import com.edissone.bymtask.context.exception.utils.ExceptionType;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    protected final ExceptionType exceptionType;

    public ServiceException(String details, ExceptionType exceptionType) {
        super(String.format(exceptionType.getMessageTemplate(), details));
        this.exceptionType = exceptionType;
    }
}
