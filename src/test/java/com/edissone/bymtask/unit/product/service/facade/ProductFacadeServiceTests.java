package com.edissone.bymtask.unit.product.service.facade;

import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.dto.ProductDTO;
import com.edissone.bymtask.product.exception.CategoryDoesNotProvidedException;
import com.edissone.bymtask.product.exception.CategoryNotFoundException;
import com.edissone.bymtask.product.service.domain.CategoryService;
import com.edissone.bymtask.product.service.domain.ProductService;
import com.edissone.bymtask.product.service.facade.ProductFacadeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Facade Service implementation tests")
public class ProductFacadeServiceTests {
    @InjectMocks
    private ProductFacadeServiceImpl facadeService;

    @Mock
    private ProductService mockProductService;

    @Mock
    private CategoryService mockCategoryService;

    @Test
    @DisplayName("Product create() - Success")
    void whenCreate_thenSuccess() {
        final var givenDTO = ProductDTO.builder().name("Test Product").price(200.00)
                .category(new ProductDTO.CategoryStub(0L, null)).sku("TSTPRD001").build();
        final var category = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();
        final var expected = ProductEntity.builder().id(0L).name("Test Product").price(200.00).sku("TSTPRD001").category(category).build();

        given(mockCategoryService.get(anyLong())).willReturn(category);
        given(mockProductService.create(any(ProductDTO.class), eq(category))).willReturn(expected);

        final var actual = facadeService.create(givenDTO);

        assertEquals(expected, actual);

        then(mockCategoryService).should(times(1)).get(anyLong());
        then(mockProductService).should(times(1)).create(any(ProductDTO.class), eq(category));
    }

    @Test
    @DisplayName("Product create(dto, category) - Failed, category not found")
    void whenCreate_thenFailed_CategoryNotFound() {
        final var givenDTO = ProductDTO.builder().name("Test Product").price(200.00).sku("TSTPRD001").build();

        assertThrows(CategoryDoesNotProvidedException.class, () -> facadeService.create(givenDTO));

        then(mockCategoryService).should(times(0)).get(anyLong());
        then(mockProductService).should(times(0)).create(any(ProductDTO.class), any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Product update(id, dto, category) - Success, without category")
    void whenUpdate_thenSuccess_withoutCategory() {
        final var givenDTO = ProductDTO.builder().name("Test Product").price(200.00).sku("TSTPRD001").build();
        final var category = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();
        final var expected = ProductEntity.builder().id(0L).name("Test Product Updated").price(250.00).sku("TSTPRD001").category(category).build();

        given(mockProductService.update(anyLong(), any(ProductDTO.class), nullable(CategoryEntity.class))).willReturn(expected);

        final var actual = facadeService.update(0L, givenDTO);

        assertEquals(expected, actual);
        then(mockCategoryService).should(times(0)).get(anyLong());
        then(mockProductService).should(times(1)).update(anyLong(), any(ProductDTO.class), nullable(CategoryEntity.class));
    }

    @Test
    @DisplayName("Product update(id, dto, category) - Success, without category")
    void whenUpdate_thenSuccess_withCategory() {
        final var givenDTO = ProductDTO.builder().name("Test Product").price(200.00).sku("TSTPRD001")
                .category(new ProductDTO.CategoryStub(1L, null)).build();
        final var category = CategoryEntity.builder().id(1L).name("Test Category 2").products(Collections.emptyList()).build();
        final var expected = ProductEntity.builder().id(0L).name("Test Product Updated").price(250.00).sku("TSTPRD001").category(category).build();

        given(mockCategoryService.get(anyLong())).willReturn(category);
        given(mockProductService.update(anyLong(), any(ProductDTO.class), any(CategoryEntity.class))).willReturn(expected);

        final var actual = facadeService.update(0L, givenDTO);

        assertEquals(expected, actual);

        then(mockCategoryService).should(times(1)).get(anyLong());
        then(mockProductService).should(times(1)).update(anyLong(), any(ProductDTO.class), any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Product update(id, dto, category) - Failed, with category not found")
    void whenUpdate_thenFailed_categoryNotFound() {
        final var givenDTO = ProductDTO.builder().name("Test Product").price(200.00).sku("TSTPRD001")
                .category(new ProductDTO.CategoryStub(2L, null)).build();

        given(mockCategoryService.get(anyLong())).willThrow(CategoryNotFoundException.class);

        assertThrows(CategoryNotFoundException.class, () -> facadeService.update(0L, givenDTO));

        then(mockCategoryService).should(times(1)).get(2L);
        then(mockProductService).should(times(0)).update(anyLong(), any(ProductDTO.class), any(CategoryEntity.class));
    }
}
