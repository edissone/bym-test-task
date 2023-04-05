package com.edissone.bymtask.product.dao.repository;

import com.edissone.bymtask.product.dao.model.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    Optional<ProductEntity> findBySku(String sku);
}
