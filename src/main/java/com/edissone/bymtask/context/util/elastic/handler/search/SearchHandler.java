package com.edissone.bymtask.context.util.elastic.handler.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchHandler<D> {
    Page<D> search(String value, Pageable pageable);
}
