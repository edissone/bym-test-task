package com.edissone.bymtask.order.service.domain;

import com.edissone.bymtask.order.dao.domain.model.OrderEntity;
import com.edissone.bymtask.order.dto.OrderDTO;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderEntity create(OrderDTO dto, List<ProductEntity> products);

    OrderEntity get(String id);

    OrderEntity update(String id, OrderDTO dto, List<ProductEntity> products);

    Page<OrderEntity> getAll(Pageable pageable);

    OrderEntity delete(String id);
}
