package com.edissone.bymtask.unit.product.service;

import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.dao.repository.ProductRepository;
import com.edissone.bymtask.product.dto.ProductDTO;
import com.edissone.bymtask.product.exception.ProductExistsException;
import com.edissone.bymtask.product.exception.ProductNotFoundException;
import com.edissone.bymtask.product.service.domain.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service implementation tests")
class ProductServiceTests {
    private static final Answer<ProductEntity> saveAnswer = invocation -> {
        final var response = (ProductEntity) invocation.getArguments()[0];
        if (response.getId() == null) {
            response.setId(0L);
        }
        return response;
    };
    @InjectMocks
    private ProductServiceImpl service;
    @Mock
    private ProductRepository mockRepository;

    @Test
    @DisplayName("Product create() - Success")
    void whenCreate_thenSuccess() {
        final var givenDTO = ProductDTO.builder().name("Test Product").price(200.00).sku("TSTPRD001").build();
        final var category = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();
        final var expected = ProductEntity.builder().id(0L).name("Test Product").price(200.00).sku("TSTPRD001").category(category).build();

        given(mockRepository.save(any(ProductEntity.class))).will(saveAnswer);
        given(mockRepository.findBySku(anyString())).willReturn(Optional.empty());

        final var actual = service.create(givenDTO, category);
        assertEquals(expected, actual);

        then(mockRepository).should(times(1)).findBySku(anyString());
        then(mockRepository).should(times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Product create(dto, category) - Failed, product with target SKU-number already exists")
    void whenCreate_thenFailed_alreadyExists() {
        final var givenDTO = ProductDTO.builder().name("Test Product").price(200.00).sku("TSTPRD001").build();
        final var category = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();
        final var exist = ProductEntity.builder().id(0L).name("Test Product").price(200.00).sku("TSTPRD001").category(category).build();

        given(mockRepository.findBySku(anyString())).willReturn(Optional.of(exist));

        assertThrows(ProductExistsException.class, () -> service.create(givenDTO, category));

        then(mockRepository).should(times(1)).findBySku(anyString());
        then(mockRepository).should(times(0)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Product get(id) - Success")
    void whenGet_thenSuccess() {
        final var category = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();
        final var expected = ProductEntity.builder().id(0L).name("Test Product").price(200.00).sku("TSTPRD001").category(category).build();

        given(mockRepository.findById(anyLong())).willReturn(Optional.of(expected));

        final var actual = service.get(0L);
        assertEquals(expected, actual);

        then(mockRepository).should(times(1)).findById(0L);
    }

    @Test
    @DisplayName("Product get(id) - Failed, product with target id not found")
    void whenGet_thenFailed_notFound() {
        given(mockRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.get(0L));

        then(mockRepository).should(times(1)).findById(0L);
    }

    @Test
    @DisplayName("Product update(id, dto, category) - Success")
    void whenUpdate_thenSuccess() {
        final var givenDTO = ProductDTO.builder().name("Test Product Updated").price(250.00).build();
        final var category = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();
        final var exist = ProductEntity.builder().id(0L).name("Test Product").price(200.00).sku("TSTPRD001").category(category).build();
        final var expected = ProductEntity.builder().id(0L).name("Test Product Updated").price(250.00).sku("TSTPRD001").category(category).build();

        given(mockRepository.save(any(ProductEntity.class))).will(saveAnswer);
        given(mockRepository.findById(anyLong())).willReturn(Optional.of(exist));

        final var actual = service.update(0L, givenDTO, category);
        assertEquals(expected, actual);

        then(mockRepository).should(times(1)).findById(anyLong());
        then(mockRepository).should(times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Product update(id, dto, category) - Failed, product with target id not found")
    void whenUpdate_thenFailed_notFound() {
        final var givenDTO = ProductDTO.builder().name("Test Product").price(200.00).sku("TSTPRD001").build();
        final var category = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();

        given(mockRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.update(0L, givenDTO, category));

        then(mockRepository).should(times(1)).findById(0L);
        then(mockRepository).should(times(0)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Product delete(id) - Success")
    void whenDelete_thenSuccess() {
        final var category = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();
        final var expected = ProductEntity.builder().id(0L).name("Test Product").price(200.00).sku("TSTPRD001").category(category).build();

        given(mockRepository.findById(anyLong())).willReturn(Optional.of(expected));

        final var actual = service.delete(0L);
        assertEquals(expected, actual);

        then(mockRepository).should(times(1)).findById(0L);
        then(mockRepository).should(times(1)).delete(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Product delete(id) - Failed, product with target id not found")
    void whenDelete_thenFailed_notFound() {
        given(mockRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.delete(0L));

        then(mockRepository).should(times(1)).findById(0L);
        then(mockRepository).should(times(0)).delete(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Product getAll() - Success")
    void whenGetAll_thenSuccess() {
        final var category = CategoryEntity.builder().id(0L).name("Test Category").build();
        final var input = List.of(
                ProductEntity.builder().id(0L).name("Test Product").price(200.00)
                        .sku("TSTPRD001").category(category).build(),
                ProductEntity.builder().id(1L).name("Test Product 2").price(250.00)
                        .sku("TSTPRD002").category(category).build());

        given(mockRepository.findAll()).willReturn(input);

        final var actual = service.getAll();

        assertEquals(input, actual);

        then(mockRepository).should(times(1)).findAll();
    }

    @Test
    @DisplayName("Product findAllByIdSet(ids) - success")
    void whenFindAllByIdSet_thenSuccess() {
        final var givenIDS = Set.of(0L, 11L, 12L);
        final var categories = List.of(
                CategoryEntity.builder().id(0L).name("Test Category").build(),
                CategoryEntity.builder().id(1L).name("Test Category 2").build()
        );

        final var result = List.of(
                ProductEntity.builder().id(0L).name("Test Product").price(200.00)
                        .sku("TSTPRD000").category(categories.get(0)).build(),
                ProductEntity.builder().id(11L).name("Test Product 2").price(210.00)
                        .sku("TSTPRD011").category(categories.get(1)).build(),
                ProductEntity.builder().id(12L).name("Test Product3").price(300.00)
                        .sku("TSTPRD012").category(categories.get(1)).build()
        );

        given(mockRepository.findAllById(anyIterable())).willReturn(result);

        final var actual = service.findAllByIdSet(givenIDS);

        assertEquals(result.size(), actual.size());
        assertTrue(actual.containsAll(result));

        then(mockRepository).should(times(1)).findAllById(anyIterable());
    }

    @Test
    @DisplayName("Product findAllByIdSet(ids) - failed, some of products not found")
    void whenFindAllByIdSet_thenFailed_multipleNotFound() {
        final var givenIDS = Set.of(0L, 2L, 3L, 6L, 11L, 12L);
        final var categories = List.of(
                CategoryEntity.builder().id(0L).name("Test Category").build(),
                CategoryEntity.builder().id(1L).name("Test Category 2").build()
        );
        final var expectedAbsentID = Set.of(2, 3, 6);

        final var result = List.of(
                ProductEntity.builder().id(0L).name("Test Product").price(200.00)
                        .sku("TSTPRD000").category(categories.get(0)).build(),
                ProductEntity.builder().id(11L).name("Test Product 2").price(210.00)
                        .sku("TSTPRD011").category(categories.get(1)).build(),
                ProductEntity.builder().id(12L).name("Test Product3").price(300.00)
                        .sku("TSTPRD012").category(categories.get(1)).build()
        );

        given(mockRepository.findAllById(anyIterable())).willReturn(result);

        final var thrown = assertThrows(ProductNotFoundException.class, () -> service.findAllByIdSet(givenIDS));
        System.out.println("expected absent = " + expectedAbsentID + "\nthrown exception message: " + thrown.getMessage());
        expectedAbsentID.forEach(it -> assertTrue(thrown.getMessage().contains(String.valueOf(it))));

        then(mockRepository).should(times(1)).findAllById(anyIterable());
    }
}
