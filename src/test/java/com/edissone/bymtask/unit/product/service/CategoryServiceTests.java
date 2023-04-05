package com.edissone.bymtask.unit.product.service;

import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dao.repository.CategoryRepository;
import com.edissone.bymtask.product.dto.CategoryDTO;
import com.edissone.bymtask.product.exception.CategoryExistException;
import com.edissone.bymtask.product.exception.CategoryNotFoundException;
import com.edissone.bymtask.product.service.domain.CategoryServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category Service implementation tests")
public class CategoryServiceTests {
    @InjectMocks
    private CategoryServiceImpl service;
    @Mock
    private CategoryRepository mockRepository;

    @Test
    @DisplayName("Category create(dto) - Success")
    void whenCreate_thenSuccess() {
        final var givenDTO = CategoryDTO.builder().name("Test Category").build();
        final var expected = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();

        given(mockRepository.save(any(CategoryEntity.class))).will(saveAnswer);
        given(mockRepository.findByName(anyString())).willReturn(Optional.empty());

        final var actual = service.create(givenDTO);
        assertEquals(expected, actual);

        then(mockRepository).should(times(1)).findByName(anyString());
        then(mockRepository).should(times(1)).save(any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Category create(dto) - Failed, category already exists")
    void whenCreate_thenFailed_alreadyExists() {
        final var givenDTO = CategoryDTO.builder().name("Test Category").build();
        final var exist = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();

        given(mockRepository.findByName(anyString())).willReturn(Optional.of(exist));

        assertThrows(CategoryExistException.class, () -> service.create(givenDTO));

        then(mockRepository).should(times(1)).findByName(anyString());
        then(mockRepository).should(times(0)).save(any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Category get(id) - Success")
    void whenGet_thenSuccess() {
        final var expected = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();

        given(mockRepository.findById(anyLong())).willReturn(Optional.of(expected));

        final var actual = service.get(0L);
        assertEquals(expected, actual);

        then(mockRepository).should(times(1)).findById(0L);
    }

    @Test
    @DisplayName("Category get(id) - Failed, category not found")
    void whenGet_thenFailed_notFound() {
        given(mockRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.get(0L));

        then(mockRepository).should(times(1)).findById(0L);
    }

    @Test
    @DisplayName("Category update(id, dto) - Success")
    void whenUpdate_thenSuccess() {
        final var givenDTO = CategoryDTO.builder().name("Test Category Updated").build();
        final var exist = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();
        final var expected = CategoryEntity.builder().id(0L).name("Test Category Updated").build();

        given(mockRepository.save(any(CategoryEntity.class))).will(saveAnswer);
        given(mockRepository.findById(anyLong())).willReturn(Optional.of(exist));

        final var actual = service.update(0L, givenDTO);
        assertEquals(expected, actual);

        then(mockRepository).should(times(1)).findById(anyLong());
        then(mockRepository).should(times(1)).save(any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Category update(id, dto) - Failed, category already exists")
    void whenUpdate_thenFailed_alreadyExists() {
        final var givenDTO = CategoryDTO.builder().name("Test Product").build();

        given(mockRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.update(0L, givenDTO));

        then(mockRepository).should(times(1)).findById(0L);
        then(mockRepository).should(times(0)).save(any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Category delete(id) - Success")
    void whenDelete_thenSuccess() {
        final var category = CategoryEntity.builder().id(0L).name("Test Category").products(Collections.emptyList()).build();

        given(mockRepository.findById(anyLong())).willReturn(Optional.of(category));

        final var actual = service.delete(0L);
        assertEquals(category, actual);

        then(mockRepository).should(times(1)).findById(0L);
        then(mockRepository).should(times(1)).delete(any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Category delete(id) - Failed, category not found")
    void whenDelete_thenFailed_notFound() {
        given(mockRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.delete(0L));

        then(mockRepository).should(times(1)).findById(0L);
        then(mockRepository).should(times(0)).delete(any(CategoryEntity.class));
    }

    @Test
    @DisplayName("Category getAll() - Success")
    void whenGetAll_thenSuccess() {
        final var input = List.of(
                CategoryEntity.builder().id(0L).name("Test Category").build(),
                CategoryEntity.builder().id(1L).name("Test Category").build()
        );

        given(mockRepository.findAll()).willReturn(input);

        final var actual = service.getAll();

        assertEquals(input, actual);

        then(mockRepository).should(times(1)).findAll();
    }

    private static final Answer<CategoryEntity> saveAnswer = invocation -> {
        final var response = (CategoryEntity) invocation.getArguments()[0];
        if (response.getId() == null) {
            response.setId(0L);
        }
        return response;
    };
}
