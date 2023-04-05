package com.edissone.bymtask.product.controller.advice;

import com.edissone.bymtask.context.exception.utils.ErrorMessage;
import com.edissone.bymtask.context.exception.ServiceException;
import com.edissone.bymtask.context.util.advice.BaseControllerAdvice;
import com.edissone.bymtask.product.controller.ProductController;
import com.edissone.bymtask.product.exception.CategoryDoesNotProvidedException;
import com.edissone.bymtask.product.exception.CategoryNotFoundException;
import com.edissone.bymtask.product.exception.ProductExistsException;
import com.edissone.bymtask.product.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = ProductController.class)
public class ProductControllerAdvice extends BaseControllerAdvice {
    @ExceptionHandler({ProductNotFoundException.class, CategoryNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFound(ServiceException exception, HttpServletRequest request) {
        return handle(exception, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleConflict(ServiceException exception, HttpServletRequest request) {
        return handle(exception, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryDoesNotProvidedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleDoesNotProvided(ServiceException ex, HttpServletRequest request) {
        return handle(ex, request, HttpStatus.BAD_REQUEST);
    }
}
