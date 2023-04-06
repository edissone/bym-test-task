package com.edissone.bymtask.context.util.advice;

import com.edissone.bymtask.context.exception.utils.ErrorMessage;
import com.edissone.bymtask.context.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Slf4j
public abstract class BaseControllerAdvice {
    protected <E extends ServiceException> ErrorMessage handle(E exception, HttpServletRequest request, HttpStatus status) {
        final var exceptionType = exception.getExceptionType().name();
        log.error("{}: {}, path={}", exceptionType, exception.getMessage(), request.getPathInfo());
        return ErrorMessage.builder()
                .message(exception.getMessage())
                .code(status.value())
                .path(request.getPathInfo())
                .exceptionType(exceptionType)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
