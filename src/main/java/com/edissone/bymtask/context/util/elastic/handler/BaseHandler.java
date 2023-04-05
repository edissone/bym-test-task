package com.edissone.bymtask.context.util.elastic.handler;

public abstract class BaseHandler {
    protected final String description;

    protected BaseHandler(String description) {
        this.description = description;
    }

    public String description(){
        return description;
    }
}
