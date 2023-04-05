package com.edissone.bymtask.product.service.mapper;

import com.edissone.bymtask.context.util.mapper.EntityMapper;
import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dto.CategoryDTO;
import com.edissone.bymtask.product.dto.ProductDTO;
import org.hibernate.LazyInitializationException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CategoryMapper implements EntityMapper<CategoryEntity, CategoryDTO> {
    @Override
    public CategoryDTO dto(CategoryEntity entity) {
        return CategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .products(extractOrEmpty(entity))
                .build();
    }

    private List<ProductDTO> extractOrEmpty(CategoryEntity entity) {
        List<ProductDTO> result;
        try {
            result = entity.getProducts().stream().map(ProductMapper::staticDTO).toList();
        } catch (LazyInitializationException | NullPointerException ex) {
            result = Collections.emptyList();
        }
        return result;
    }
}
