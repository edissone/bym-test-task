package com.edissone.bymtask.api.order;

import com.edissone.bymtask.api.BaseApiTest;
import com.edissone.bymtask.context.exception.utils.ErrorMessage;
import com.edissone.bymtask.order.dao.elastic.model.OrderDocument;
import com.edissone.bymtask.order.dao.embedded.model.OrderItem;
import com.edissone.bymtask.order.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderAPITests extends BaseApiTest {
    private static final String API = "/orders";

    @Autowired
    private MockMvc mvc;

    @BeforeAll
    static void setup() throws IOException, URISyntaxException {
        data = loadData("data/test-data.json");
    }

    @Test
    @Order(2)
    @DisplayName("Success GET /api/orders/:id")
    void whenGet_thenSuccess() throws Exception {
        final var TEST_LABEL = "success-get-order";
        final var input = data.get(TEST_LABEL);
        final var expect = input.expect(OrderDTO.class);
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + input.getGiven_params() + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        get(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, OrderDTO.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNotNull(actual.getItems());
        assertEquals(expect.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expect.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expect.getAmount(), actual.getAmount());
        assertEquals(expect.getItems().size(), actual.getItems().size());
        assertEqualNestedItems(expect.getItems().stream(), actual.getItems().stream());
    }

    @Test
    @Order(1)
    @DisplayName("Fail - GET /api/orders/:id - order not found")
    void whenGet_thenFailed_NotFound() throws Exception {
        final var TEST_LABEL = "get-failed-not-found";
        final var input = data.get(TEST_LABEL);
        final var expect = input.expect_error();
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + input.getGiven_params() + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        get(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, ErrorMessage.class);

        assertErrorMessage(expect, actual);
    }

    @Test
    @Order(2)
    @DisplayName("Success DELETE /api/orders/:id")
    void whenDelete_thenSuccess() throws Exception {
        final var TEST_LABEL = "success-delete-order";
        final var input = data.get(TEST_LABEL);
        final var expect = input.expect(OrderDTO.class);
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + input.getGiven_params() + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        delete(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, OrderDTO.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNotNull(actual.getItems());
        assertEquals(expect.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expect.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(expect.getAmount(), actual.getAmount());
        assertEquals(expect.getItems().size(), actual.getItems().size());
        assertEqualNestedItems(expect.getItems().stream(), actual.getItems().stream());
    }

    @Test
    @Order(2)
    @DisplayName("Fail - DELETE /api/orders/:id - order not found")
    void whenDelete_thenFailed_NotFound() throws Exception {
        final var TEST_LABEL = "delete-failed-not-found";
        final var input = data.get(TEST_LABEL);
        final var expect = input.expect_error();
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + input.getGiven_params() + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        delete(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, ErrorMessage.class);

        assertErrorMessage(expect, actual);
    }


    @Test
    @DisplayName("Success POST /api/orders")
    void whenCreate_thenSuccess() throws Exception {
        final var TEST_LABEL = "success-create-order";
        final var input = data.get(TEST_LABEL);
        final var given = input.given();
        final var expect = input.expect(OrderDTO.class);
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + given + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        post(url)
                                .content(given)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, OrderDTO.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNotNull(actual.getCreatedAt());
        assertNotNull(actual.getUpdatedAt());
        assertNotNull(actual.getItems());
        assertEquals(expect.getAmount(), actual.getAmount());
        assertEquals(expect.getItems().size(), actual.getItems().size());
        assertEqualNestedItems(expect.getItems().stream(), actual.getItems().stream());
    }

    @Test
    @DisplayName("Fail - POST /api/orders - one of nested items product id not found")
    void whenCreate_thenFailed_nestedNotFound() throws Exception {
        final var TEST_LABEL = "create-failed-nested-products-not-found";
        final var input = data.get(TEST_LABEL);
        final var given = input.given();
        final var expect = input.expect_error();
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + given + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        post(url)
                                .content(given)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, ErrorMessage.class);

        assertErrorMessage(expect, actual);
    }

    @Test
    @DisplayName("Success PUT /api/orders/:id")
    void whenUpdate_thenSuccess() throws Exception {
        final var TEST_LABEL = "success-update-order";
        final var input = data.get(TEST_LABEL);
        final var given = input.given();
        final var expect = input.expect(OrderDTO.class);
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + given + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        put(url)
                                .content(given)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, OrderDTO.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertNotNull(actual.getCreatedAt());
        assertNotNull(actual.getUpdatedAt());
        assertNotNull(actual.getItems());
        assertEquals(expect.getAmount(), actual.getAmount());
        assertEquals(expect.getItems().size(), actual.getItems().size());
        assertEqualNestedItems(expect.getItems().stream(), actual.getItems().stream());
    }

    @Test
    @DisplayName("Fail - PUT /api/orders/:id - one of nested items product id not found")
    void whenUpdate_thenFailed_nestedNotFound() throws Exception {
        final var TEST_LABEL = "update-failed-one-of-nested-products-not-found";
        final var input = data.get(TEST_LABEL);
        final var given = input.given();
        final var expect = input.expect_error();
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + given + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        put(url)
                                .content(given)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, ErrorMessage.class);

        assertErrorMessage(expect, actual);
    }

    @Test
    @DisplayName("Fail - PUT /api/orders/:id - order not found")
    void whenUpdate_thenFailed_NotFound() throws Exception {
        final var TEST_LABEL = "update-failed-order-not-found";
        final var input = data.get(TEST_LABEL);
        final var given = input.given();
        final var expect = input.expect_error();
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + given + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        put(url)
                                .content(given)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, ErrorMessage.class);

        assertErrorMessage(expect, actual);
    }

    @Test
    @Order(1)
    @DisplayName("Success search [item-product-name-like]")
    void whenSearchPNameLike_thenSuccess() throws Exception {
        final var TEST_LABEL = "success-search-item-product-name-like";
        final var input = data.get(TEST_LABEL);
        final var expectMap = input.expect(new TypeReference<Map<String, Object>>() {
        });
        final var url = input.prepareURL(API);

        final var expectValue = (String) expectMap.get("value");
        final var expectQuery = (String) expectMap.get("query_type");
        final var expect = unwrapDocumentsResponse(expectMap, OrderDocument.class);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + input.getGiven_params() + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        get(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actualMap = mapper.readValue(actualRaw, new TypeReference<Map<String, Object>>() {
        });
        final var actualQuery = (String) actualMap.get("query_type");
        final var actualValue = (String) actualMap.get("value");
        final var actual = unwrapDocumentsResponse(actualMap, OrderDocument.class);

        assertEquals(expectValue, actualValue);
        assertEquals(expectQuery, actualQuery);
        assertEqualsDocumentStreams(expect.stream(), actual.stream());
    }

    @Test
    @DisplayName("Fail - search :invalid_type - Query type handler not implemented")
    void whenSearch_thenFailed_NotImplemented() throws Exception {
        final var TEST_LABEL = "search-failed-handler-not-implemented";
        final var input = data.get(TEST_LABEL);
        final var expect = input.expect_error();
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + input.getGiven_params() + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        get(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotImplemented())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, ErrorMessage.class);

        assertErrorMessage(expect, actual);
    }

    @Test
    @DisplayName("Fail - report :invalid_type - Report type handler not implemented")
    void whenReport_thenFailed_NotImplemented() throws Exception {
        final var TEST_LABEL = "report-failed-handler-not-implemented";
        final var input = data.get(TEST_LABEL);
        final var expect = input.expect_error();
        final var url = input.prepareURL(API);

        System.out.println(TEST_LABEL + ":\n\tgiven=" + input.getGiven_params() + "\n\texpect=" + expect);

        final var actualRaw = mvc.perform(
                        get(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotImplemented())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final var actual = mapper.readValue(actualRaw, ErrorMessage.class);

        assertErrorMessage(expect, actual);
    }


    // OrderDocumentAssertions

    private void assertEqualsDocumentStreams(Stream<OrderDocument> expect, Stream<OrderDocument> actual) {
        final var expectDOCtoID = expect.collect(Collectors.toMap(OrderDocument::getId, Function.identity()));
        final var actualDOCtoID = actual.collect(Collectors.toMap(OrderDocument::getId, Function.identity()));

        assertEquals(expectDOCtoID.keySet().size(), actualDOCtoID.keySet().size());
        assertTrue(actualDOCtoID.keySet().containsAll(expectDOCtoID.keySet()));

        actualDOCtoID.values().forEach(assertEqualsDocConsumer(expectDOCtoID));
    }

    private Consumer<OrderDocument> assertEqualsDocConsumer(Map<String, OrderDocument> expect) {
        return a -> {
            final var e = expect.get(a.getId());
            assertNotNull(a);
            assertNotNull(a.getId());
            assertNotNull(a.getCreatedAt());
            assertNotNull(a.getUpdatedAt());
            assertNotNull(a.getItems());
            assertEquals(e.getAmount(), a.getAmount());
            assertEquals(e.getItems().size(), a.getItems().size());
            assertEqualNestedItemsDoc(e.getItems().stream(), a.getItems().stream());
        };
    }

    private Consumer<OrderItem> assertItemDocEquals(Map<Long, OrderItem> expect) {
        return a -> {
            final var e = expect.get(a.getProductId());
            assertEquals(e.getProductPrice(), a.getProductPrice());
            assertEquals(e.getProductSku(), a.getProductSku());
            assertEquals(e.getProductName(), a.getProductName());
            assertEquals(e.getQuantity(), a.getQuantity());
        };
    }

    private void assertEqualNestedItemsDoc(Stream<OrderItem> expect, Stream<OrderItem> actual) {
        final var expectProductIdToItem = expect.collect(Collectors.toMap(OrderItem::getProductId, Function.identity()));
        final var actualProductIdToItem = actual.collect(Collectors.toMap(OrderItem::getProductId, Function.identity()));

        assertEquals(expectProductIdToItem.keySet().size(), actualProductIdToItem.keySet().size());
        assertTrue(actualProductIdToItem.keySet().containsAll(expectProductIdToItem.keySet()));

        actualProductIdToItem.values().forEach(assertItemDocEquals(expectProductIdToItem));
    }


    // OrderDTO assertions

    private void assertEqualsDTOStreams(Stream<OrderDTO> expect, Stream<OrderDTO> actual) {
        final var expectDTOtoID = expect.collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
        final var actualDTOtoID = actual.collect(Collectors.toMap(OrderDTO::getId, Function.identity()));

        assertEquals(expectDTOtoID.keySet().size(), actualDTOtoID.keySet().size());
        assertTrue(actualDTOtoID.keySet().containsAll(expectDTOtoID.keySet()));

        actualDTOtoID.values().forEach(assertEqualsDTOConsumer(expectDTOtoID));
    }

    private Consumer<OrderDTO> assertEqualsDTOConsumer(Map<String, OrderDTO> expect) {
        return a -> {
            final var e = expect.get(a.getId());
            assertNotNull(a);
            assertNotNull(a.getId());
            assertNotNull(a.getCreatedAt());
            assertNotNull(a.getUpdatedAt());
            assertNotNull(a.getItems());
            assertEquals(e.getAmount(), a.getAmount());
            assertEquals(e.getItems().size(), a.getItems().size());
            assertEqualNestedItems(e.getItems().stream(), a.getItems().stream());
        };
    }

    private void assertEqualNestedItems(Stream<OrderDTO.OrderItemDTO> expect, Stream<OrderDTO.OrderItemDTO> actual) {
        final var expectProductIdToItem = expect.collect(Collectors.toMap(OrderDTO.OrderItemDTO::getProductId, Function.identity()));
        final var actualProductIdToItem = actual.collect(Collectors.toMap(OrderDTO.OrderItemDTO::getProductId, Function.identity()));

        assertEquals(expectProductIdToItem.keySet().size(), actualProductIdToItem.keySet().size());
        assertTrue(actualProductIdToItem.keySet().containsAll(expectProductIdToItem.keySet()));

        actualProductIdToItem.values().forEach(assertItemEquals(expectProductIdToItem));
    }

    private Consumer<OrderDTO.OrderItemDTO> assertItemEquals(Map<Long, OrderDTO.OrderItemDTO> expect) {
        return a -> {
            final var e = expect.get(a.getProductId());
            assertEquals(e.getProductPrice(), a.getProductPrice());
            assertEquals(e.getProductSku(), a.getProductSku());
            assertEquals(e.getProductName(), a.getProductName());
            assertEquals(e.getQuantity(), a.getQuantity());
        };
    }

    // ErrorMessage assertions

    private void assertErrorMessage(ErrorMessage expect, ErrorMessage actual) {
        assertEquals(expect.getMessage(), actual.getMessage());
        assertEquals(expect.getExceptionType(), actual.getExceptionType());
        assertEquals(expect.getPath(), actual.getPath());
        assertEquals(expect.getCode(), actual.getCode());
        assertNotNull(actual.getTimestamp());
    }

    private <T> List<T> unwrapDocumentsResponse(Map<String, Object> map, Class<T> type) throws JsonProcessingException {
        return (
                (List<?>) mapper.readValue(
                                mapper.writeValueAsString(
                                        map.get("results")), new TypeReference<Map<String, Object>>() {
                                })
                        .get("content")
        ).stream().map(mapCatchable(type)).toList();
    }

    // I got much pain from Jackson. It's unable to deserialize Page due to absent public constructors. Sorry ;)

    private <T> Function<Object, T> mapCatchable(Class<T> type) {
        return it -> {
            try {
                return mapper.readValue(mapper.writeValueAsString(it), type);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
