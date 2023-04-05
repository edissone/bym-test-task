package com.edissone.bymtask.order.dao.domain.repository;

import com.edissone.bymtask.order.dao.domain.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
}
