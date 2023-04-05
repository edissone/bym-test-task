package com.edissone.bymtask.product.controller;

import com.edissone.bymtask.context.util.mapper.EntityMapper;
import com.edissone.bymtask.product.dao.model.CategoryEntity;
import com.edissone.bymtask.product.dto.CategoryDTO;
import com.edissone.bymtask.product.service.domain.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;
    private final EntityMapper<CategoryEntity, CategoryDTO> mapper;

    @Autowired
    public CategoryController(CategoryService service, EntityMapper<CategoryEntity, CategoryDTO> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public CategoryDTO get(@RequestParam("id") Long id) {
        return mapper.dto(service.get(id));
    }

    @GetMapping("/all")
    public List<CategoryDTO> all(){
        return service.getAll().stream().map(mapper::dto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO create(@RequestBody CategoryDTO dto) {
        return mapper.dto(service.create(dto));
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        return mapper.dto(service.update(id, dto));
    }

    @DeleteMapping
    public CategoryDTO delete(@RequestParam("id") Long id) {
        return mapper.dto(service.delete(id));
    }
}
