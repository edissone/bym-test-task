package com.edissone.bymtask.product.exception;

import com.edissone.bymtask.context.exception.utils.ExceptionType;
import com.edissone.bymtask.context.exception.ServiceException;

public class ProductExistsException extends ServiceException {
    public ProductExistsException(String details) {
        super(details, ExceptionType.PRODUCT_EXIST);
    }
}
