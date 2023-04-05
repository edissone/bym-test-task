package com.edissone.bymtask.product.exception;

import com.edissone.bymtask.context.exception.utils.ExceptionType;
import com.edissone.bymtask.context.exception.ServiceException;

public class CategoryNotFoundException extends ServiceException {
    public CategoryNotFoundException(String details) {
        super(details, ExceptionType.CATEGORY_NOT_FOUND);
    }
}
