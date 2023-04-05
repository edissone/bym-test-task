package com.edissone.bymtask.order.dto;

import com.edissone.bymtask.order.dao.elastic.model.OrderDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderSearchResponse {
    @JsonProperty("query_type")
    private String queryType;
    private String value;
    private Page<OrderDocument> results;
}
