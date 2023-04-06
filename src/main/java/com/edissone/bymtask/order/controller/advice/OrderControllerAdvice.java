package com.edissone.bymtask.order.controller.advice;

import com.edissone.bymtask.context.exception.UnrecognizedHandlerTypeException;
import com.edissone.bymtask.context.exception.utils.ErrorMessage;
import com.edissone.bymtask.context.exception.ServiceException;
import com.edissone.bymtask.context.exception.utils.ExceptionType;
import com.edissone.bymtask.context.util.advice.BaseControllerAdvice;
import com.edissone.bymtask.order.controller.OrderController;
import com.edissone.bymtask.order.exception.OrderNotFoundException;
import com.edissone.bymtask.context.exception.HandlerNotImplementedException;
import com.edissone.bymtask.product.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = OrderController.class)
public class OrderControllerAdvice extends BaseControllerAdvice {
    @ExceptionHandler({OrderNotFoundException.class, ProductNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFound(ServiceException ex, HttpServletRequest request) {
        return handle(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({HandlerNotImplementedException.class})
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public ErrorMessage handleNotImplemented(ServiceException ex, HttpServletRequest request){
        return handle(ex, request, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler({UnrecognizedHandlerTypeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleUnrecognizedHandler(ServiceException ex, HttpServletRequest request){
        return handle(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleInternal(RuntimeException ex, HttpServletRequest request){
        return handle(
                new ServiceException(ex.getMessage(), ExceptionType.INTERNAL_ERROR),
                request, HttpStatus.INTERNAL_SERVER_ERROR
         );
    }
}