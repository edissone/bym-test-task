package com.edissone.bymtask.product.controller.advice;

import com.edissone.bymtask.context.exception.utils.ErrorMessage;
import com.edissone.bymtask.context.exception.ServiceException;
import com.edissone.bymtask.context.util.advice.BaseControllerAdvice;
import com.edissone.bymtask.product.controller.CategoryController;
import com.edissone.bymtask.product.exception.CategoryExistException;
import com.edissone.bymtask.product.exception.CategoryNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = CategoryController.class)
public class CategoryControllerAdvice extends BaseControllerAdvice {
    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFound(ServiceException ex, HttpServletRequest request) {
        return handle(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleConflict(ServiceException ex, HttpServletRequest request){
        return handle(ex, request, HttpStatus.CONFLICT);
    }
}
