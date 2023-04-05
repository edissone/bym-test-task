package com.edissone.bymtask.context.exception;

import com.edissone.bymtask.context.exception.utils.ExceptionType;

public class UnrecognizedHandlerTypeException extends ServiceException{
    public UnrecognizedHandlerTypeException(String handlerType) {
        super(handlerType, ExceptionType.HANDLER_TYPE_UNRECOGNIZED);
    }
}
