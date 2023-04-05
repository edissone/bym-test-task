package com.edissone.bymtask.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    @JsonProperty(value = "category_id")
    private Long id;
    @JsonProperty(value = "category_name")
    private String name;
    private List<ProductDTO> products;
}
