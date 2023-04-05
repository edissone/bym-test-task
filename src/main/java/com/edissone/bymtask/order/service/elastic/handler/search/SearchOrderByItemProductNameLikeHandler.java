package com.edissone.bymtask.order.service.elastic.handler.search;

import com.edissone.bymtask.context.util.elastic.handler.search.BaseSearchHandler;
import com.edissone.bymtask.order.dao.elastic.model.OrderDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service("item-product-name-like")
public class SearchOrderByItemProductNameLikeHandler extends BaseSearchHandler<OrderDocument> {
    @Autowired
    public SearchOrderByItemProductNameLikeHandler(ElasticsearchOperations template) {
        super(template, "Query type performing search for all orders contains product with target part in product name");
    }

    @Override
    public Page<OrderDocument> search(String value, Pageable pageable) {
        return doSearch("{\"bool\": {\"must\": [{\"nested\": {\"path\": \"order_items\", \"query\": {\"wildcard\"" +
                ": {\"order_items.product_name\": \"*" + value.toLowerCase() + "*\"}}}}]}}", pageable, OrderDocument.class);
    }
}
