package com.edissone.bymtask.context.util.elastic.handler.search;

import com.edissone.bymtask.context.util.elastic.handler.BaseHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;

public abstract class BaseSearchHandler<D> extends BaseHandler implements SearchHandler<D> {
    protected final ElasticsearchOperations template;

    protected BaseSearchHandler(ElasticsearchOperations template, String description) {
        super(description);
        this.template = template;
    }



    protected Page<D> doSearch(String query, Pageable pageable, Class<D> type) {
        final var searchQuery = new NativeQueryBuilder()
                .withQuery(QueryBuilders.wrapperQuery(query)._toQuery())
                .withPageable(pageable)
                .build();

        final var hits = template.search(searchQuery, type)
                .getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();
        return new PageImpl<>(hits, pageable, hits.size());
    }
}
