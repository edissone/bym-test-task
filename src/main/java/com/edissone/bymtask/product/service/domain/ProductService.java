package com.edissone.bymtask.product.service.domain;

import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.dto.ProductDTO;

import java.util.List;
import java.util.Set;

public interface ProductService {
    ProductEntity create(ProductDTO dto, CategoryEntity category);

    ProductEntity get(Long id);

    List<ProductEntity> getAll();

    List<ProductEntity> findAllByIdSet(Set<Long> ids);

    ProductEntity update(Long id, ProductDTO dto, CategoryEntity category);

    ProductEntity delete(Long id);
}
