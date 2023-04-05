package com.edissone.bymtask.unit.product.service.mapper;

import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.dto.ProductDTO;
import com.edissone.bymtask.product.service.mapper.ProductMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Product Mapper implementation tests")
class ProductMapperTests {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    @DisplayName("Product mapper staticDto(entity) - Success")
    void whenStaticDTO_thenSuccess() {
        final var entity = ProductEntity.builder()
                .id(10L).name("Test Product").price(200.00).sku("TTKKSW004")
                .category(new CategoryEntity(0L, "category-test", Collections.emptyList()))
                .build();
        final var expected = ProductDTO.builder()
                .id(10L).name("Test Product").price(200.00).sku("TTKKSW004")
                .build();


        final var actual = ProductMapper.staticDTO(entity);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Product mapper dto(entity) - Success")
    void whenDto_thenSuccess() {
        final var entity = ProductEntity.builder()
                .id(10L).name("Test Product").price(200.00).sku("TTKKSW004")
                .category(new CategoryEntity(0L, "category-test", Collections.emptyList()))
                .build();
        final var expected = ProductDTO.builder()
                .id(10L).name("Test Product").price(200.00).sku("TTKKSW004")
                .category(new ProductDTO.CategoryStub(0L, "category-test"))
                .build();


        final var actual = mapper.dto(entity);

        assertEquals(expected, actual);
    }
    @Test
    @DisplayName("Product mapper dto(entity) - Success, category is null")
    void whenDto_thenSuccess_nullCategory() {
        final var entity = ProductEntity.builder()
                .id(10L).name("Test Product").price(200.00).sku("TTKKSW004")
                .category(null)
                .build();
        final var expected = ProductDTO.builder()
                .id(10L).name("Test Product").price(200.00).sku("TTKKSW004")
                .build();


        final var actual = mapper.dto(entity);

        assertEquals(expected, actual);
    }
}