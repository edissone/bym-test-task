package com.edissone.bymtask.context.exception;

import com.edissone.bymtask.context.exception.utils.ExceptionType;

public class HandlerNotImplementedException extends ServiceException {

    public HandlerNotImplementedException(String details) {
        super(details, ExceptionType.HANDLER_NOT_IMPLEMENTED);
    }
}
