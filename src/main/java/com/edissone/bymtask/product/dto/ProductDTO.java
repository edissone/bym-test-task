package com.edissone.bymtask.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ProductDTO {
    @JsonProperty("product_id")
    private Long id;
    @JsonProperty("product_name")
    private String name;
    @JsonProperty("product_price")
    private Double price;
    @JsonProperty("product_sku")
    private String sku;
    @JsonProperty("product_category")
    private CategoryStub category;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class CategoryStub {
        @JsonProperty("category_id")
        private Long categoryId;
        @JsonProperty("category_name")
        private String categoryName;
    }
}
