package com.edissone.bymtask.product.service.facade;

import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.dto.ProductDTO;

import java.util.List;

public interface ProductFacadeService {
    ProductEntity create(ProductDTO dto);

    ProductEntity get(Long id);

    List<ProductEntity> getAll();

    ProductEntity update(Long id, ProductDTO dto);

    ProductEntity delete(Long id);
}
