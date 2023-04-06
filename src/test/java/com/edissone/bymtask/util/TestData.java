package com.edissone.bymtask.util;

import com.edissone.bymtask.context.exception.utils.ErrorMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestData {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    private String test;
    private Data data;

    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonRawValue
        private Object given;
        private String given_params;
        private ErrorMessage expect_error;
        @JsonRawValue
        private Object expect;

        public String prepareURL(String url) {
            return given_params != null ? url + given_params : url;
        }

        public ErrorMessage expect_error() {
            return expect_error;
        }

        public String given() throws JsonProcessingException {
            return mapper.writeValueAsString(this.given);
        }

        public String expect() throws JsonProcessingException {
            return mapper.writeValueAsString(this.expect);
        }

        public <T> T given(Class<T> type) throws JsonProcessingException {
            return mapToObject(this.given, type);
        }

        public <T> T expect(Class<T> type) throws JsonProcessingException {
            return mapToObject(this.expect, type);
        }

        public <T> T expect(TypeReference<T> type) throws JsonProcessingException {
            return mapToObject(this.expect, type);
        }

        private <T> T mapToObject(Object value, Class<T> type) throws JsonProcessingException {
            return mapper.readValue(mapper.writeValueAsString(value), type);
        }

        private <T> T mapToObject(Object value, TypeReference<T> type) throws JsonProcessingException {
            return mapper.readValue(mapper.writeValueAsString(value), type);
        }
    }
}
