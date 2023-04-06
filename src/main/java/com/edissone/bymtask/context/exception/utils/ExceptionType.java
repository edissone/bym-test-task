package com.edissone.bymtask.context.exception.utils;

import lombok.Getter;

@Getter
public enum ExceptionType {
    PRODUCT_NOT_FOUND("Product with %s not found"),
    PRODUCT_EXIST("Product with %s exists"),

    CATEGORY_NOT_FOUND("Category with %s not found"),
    CATEGORY_EXIST("Category with %s exists"),
    CATEGORY_DOES_NOT_PROVIDED("Category does not provided!%s"),

    ORDER_NOT_FOUND("Order with %s not found"),

    HANDLER_NOT_IMPLEMENTED("Handler for [%s] not implemented"),
    HANDLER_TYPE_UNRECOGNIZED("Unable to recognize [%s] handler type"),

    INTERNAL_ERROR("Internal server error: %s");

    private final String messageTemplate;

    ExceptionType(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }
}
