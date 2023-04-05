package com.edissone.bymtask.context.util.mapper;

public interface EntityMapper<E, D> {
    D dto(E entity);
}
