package com.edissone.bymtask.api;

import com.edissone.bymtask.util.TestData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BaseApiTest {
    protected static final ObjectMapper mapper = new ObjectMapper();
    protected static Map<String, TestData.Data> data;

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    protected static Map<String, TestData.Data> loadData(String path) throws IOException, URISyntaxException {
        return mapper.readValue(Paths.get(ClassLoader.getSystemResource(path).toURI()).toFile(),
                        new TypeReference<List<TestData>>() {
                }).stream()
                .collect(Collectors.toMap(TestData::getTest, TestData::getData));
    }
}
