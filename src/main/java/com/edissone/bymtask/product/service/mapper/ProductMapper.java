package com.edissone.bymtask.product.service.mapper;

import com.edissone.bymtask.context.util.mapper.EntityMapper;
import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements EntityMapper<ProductEntity, ProductDTO> {
    /**
     * Using only for static call from {@link CategoryMapper#dto(CategoryEntity)} as inner dtos
     */
    public static ProductDTO staticDTO(ProductEntity entity) {
        return ProductDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .sku(entity.getSku())
                .build();
    }

    @Override
    public ProductDTO dto(ProductEntity entity) {
        return ProductDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .sku(entity.getSku())
                .category(
                        entity.getCategory() != null ?
                                new ProductDTO.CategoryStub(
                                        entity.getCategory().getId(),
                                        entity.getCategory().getName()
                                ) : null
                )
                .build();
    }
}
