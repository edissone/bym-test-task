package com.edissone.bymtask.product.service.domain;

import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryEntity create(CategoryDTO dto);

    CategoryEntity get(Long id);

    List<CategoryEntity> getAll();

    CategoryEntity update(Long id, CategoryDTO dto);

    CategoryEntity delete(Long id);
}
