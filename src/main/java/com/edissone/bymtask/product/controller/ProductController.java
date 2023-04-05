package com.edissone.bymtask.product.controller;

import com.edissone.bymtask.context.util.mapper.EntityMapper;
import com.edissone.bymtask.product.dao.model.ProductEntity;
import com.edissone.bymtask.product.dto.ProductDTO;
import com.edissone.bymtask.product.service.facade.ProductFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final EntityMapper<ProductEntity, ProductDTO> mapper;
    private final ProductFacadeService service;

    @Autowired
    public ProductController(EntityMapper<ProductEntity, ProductDTO> mapper, ProductFacadeService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductDTO dto) {
        return mapper.dto(service.create(dto));
    }

    @GetMapping
    public ProductDTO get(@RequestParam("id") Long id) {
        return mapper.dto(service.get(id));
    }

    @GetMapping("/all")
    public List<ProductDTO> all() {
        return service.getAll().stream().map(mapper::dto).toList();
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        return mapper.dto(service.update(id, dto));
    }

    @DeleteMapping
    public ProductDTO delete(@RequestParam("id") Long id) {
        return mapper.dto(service.delete(id));
    }
}
