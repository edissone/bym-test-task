package com.edissone.bymtask.order.service.elastic.handler.search;

import com.edissone.bymtask.context.util.elastic.handler.search.BaseSearchHandler;
import com.edissone.bymtask.order.dao.elastic.model.OrderDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service("total-amount-gt")
public class SearchWhereAmountGTEHandler extends BaseSearchHandler<OrderDocument> {

    @Autowired
    public SearchWhereAmountGTEHandler(ElasticsearchOperations template) {
        super(template, "Query type performing search for all orders with much amount then target value");
    }

    @Override
    public Page<OrderDocument> search(String value, Pageable pageable) {
        return doSearch("{\"range\": {\"order_amount\": {\"gt\": " + value + "}}}", pageable, OrderDocument.class);
    }
}
