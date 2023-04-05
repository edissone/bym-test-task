package com.edissone.bymtask.unit.product.service.mapper;

import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.dto.CategoryDTO;
import com.edissone.bymtask.product.dto.ProductDTO;
import com.edissone.bymtask.product.service.mapper.CategoryMapper;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@DisplayName("Category Mapper implementation tests")
public class CategoryMapperTests {

    private final CategoryMapper mapper = new CategoryMapper();

    @Test
    @DisplayName("Category mapper dto(entity) - Success, with products")
    void whenDTO_thenSuccess_withProducts() {
        final var entity = CategoryEntity.builder()
                .id(10L).name("category").build();

        final var products = List.of(
                new ProductEntity(1L, "PE1", 1.00, "PE1", entity),
                new ProductEntity(2L, "PE2", 2.00, "PE2", entity)
        );

        entity.setProducts(products);

        final var expected = CategoryDTO.builder()
                .id(10L).name("category").products(
                        List.of(
                                new ProductDTO(1L, "PE1", 1.00, "PE1", null),
                                new ProductDTO(2L, "PE2", 2.00, "PE2", null)
                        )
                )
                .build();


        final var actual = mapper.dto(entity);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Category mapper dto(entity) - Success, without products")
    void whenDTO_thenSuccess_withoutProducts() {
        final var entity = spy(CategoryEntity.builder()
                .id(10L).name("category").build());

        final var products = List.of(
                new ProductEntity(1L, "PE1", 1.00, "PE1", entity),
                new ProductEntity(2L, "PE2", 2.00, "PE2", entity));

        entity.setProducts(products);

        given(entity.getProducts()).willThrow(LazyInitializationException.class);

        final var expected = CategoryDTO.builder()
                .id(10L).name("category").products(Collections.emptyList())
                .build();


        final AtomicReference<CategoryDTO> actual = new AtomicReference<>();
        assertDoesNotThrow(() -> actual.set(mapper.dto(entity)));

        assertEquals(expected, actual.get());
    }
}
