package com.edissone.bymtask.order.dao.elastic.repository;

import com.edissone.bymtask.order.dao.elastic.model.OrderDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrderElasticRepository extends ElasticsearchRepository<OrderDocument, String> {
}
