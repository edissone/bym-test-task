package com.edissone.bymtask.product.exception;

import com.edissone.bymtask.context.exception.utils.ExceptionType;
import com.edissone.bymtask.context.exception.ServiceException;

public class CategoryDoesNotProvidedException extends ServiceException {
    public CategoryDoesNotProvidedException() {
        super("", ExceptionType.CATEGORY_DOES_NOT_PROVIDED);
    }
}
