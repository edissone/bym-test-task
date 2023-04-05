package com.edissone.bymtask.product.exception;

import com.edissone.bymtask.context.exception.utils.ExceptionType;
import com.edissone.bymtask.context.exception.ServiceException;

public class ProductNotFoundException extends ServiceException {
    public ProductNotFoundException(String details) {
        super(details, ExceptionType.PRODUCT_NOT_FOUND);
    }
}
