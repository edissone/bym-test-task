package com.edissone.bymtask.product.service.domain;

import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dao.repository.CategoryRepository;
import com.edissone.bymtask.product.dto.CategoryDTO;
import com.edissone.bymtask.product.exception.CategoryExistException;
import com.edissone.bymtask.product.exception.CategoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryEntity create(CategoryDTO dto) {
        final var exist = repository.findByName(dto.getName());
        exist.ifPresent(it -> {
            throw new CategoryExistException("name" + dto.getName());
        });

        final var insertable = CategoryEntity.builder()
                .name(dto.getName())
                .build();

        final var saved = repository.save(insertable);
        log.info("create category: {}", saved);
        return saved;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public CategoryEntity get(Long id) {
        return repository.findById(id).orElseThrow(() -> new CategoryNotFoundException("id=" + id));
    }

    @Override
    public List<CategoryEntity> getAll() {
        final var results = new LinkedList<CategoryEntity>();
        repository.findAll().forEach(results::add);
        return results;
    }

    @Override
    @Transactional
    public CategoryEntity update(Long id, CategoryDTO dto) {
        final var exist = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("id=" + id));

        final var updatable = CategoryEntity.builder()
                .id(exist.getId())
                .name(dto.getName() != null ? dto.getName() : exist.getName())
                .build();

        final var saved = repository.save(updatable);
        log.info("update category: {} \n-> {}", exist, saved);
        return saved;
    }

    @Override
    public CategoryEntity delete(Long id) {
        final var exist = repository.findById(id).orElseThrow(() -> new CategoryNotFoundException("id=" + id));
        repository.delete(exist);
        log.info("delete category: {}", exist);
        return exist;
    }
}
