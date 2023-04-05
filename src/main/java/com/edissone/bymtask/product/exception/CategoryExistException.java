package com.edissone.bymtask.product.exception;

import com.edissone.bymtask.context.exception.utils.ExceptionType;
import com.edissone.bymtask.context.exception.ServiceException;

public class CategoryExistException extends ServiceException {
    public CategoryExistException(String details) {
        super(details, ExceptionType.CATEGORY_EXIST);
    }
}
