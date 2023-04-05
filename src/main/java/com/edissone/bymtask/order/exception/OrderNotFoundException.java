package com.edissone.bymtask.order.exception;

import com.edissone.bymtask.context.exception.utils.ExceptionType;
import com.edissone.bymtask.context.exception.ServiceException;

public class OrderNotFoundException extends ServiceException {
    public OrderNotFoundException(String details) {
        super(details, ExceptionType.ORDER_NOT_FOUND);
    }
}