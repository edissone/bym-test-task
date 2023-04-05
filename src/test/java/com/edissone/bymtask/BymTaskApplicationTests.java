package com.edissone.bymtask;

import com.edissone.bymtask.context.util.elastic.handler.report.ReportHandler;
import com.edissone.bymtask.context.util.elastic.handler.search.SearchHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BymTaskApplicationTests {
    @Autowired
    private ApplicationContext ctx;

    @Test
    void contextLoads() {
    }

    @TestFactory
    @DisplayName("Get all search handlers to their names - success")
    Stream<DynamicTest> searchHandlersLoad() {
        final var services = List.of(
                new String[]{"total-amount-gt", "SearchWhereAmountGTEHandler"},
                new String[]{"item-product-name-like", "SearchOrderByItemProductNameLikeHandler"}
        );

        final var testLabel = "Get all search handlers to their names - success: ";

        return services.stream().map(input ->
                DynamicTest.dynamicTest(
                        testLabel + Arrays.toString(input),
                        () -> assertLoad(SearchHandler.class, input)
                )
        );

    }

    @TestFactory
    @DisplayName("Get all report handlers to their names - success")
    Stream<DynamicTest> reportHandlersLoad() {
        final var services = List.of(
                new String[]{"total-amount-per-day", "TotalAmountPerDayReportHandler"},
                new String[]{"total-amount-per-day", "TotalAmountPerDayReportHandler"} // todo replace to other
        );

        final var testLabel = "Get all search handlers to their names - success: ";

        return services.stream().map(input ->
                DynamicTest.dynamicTest(
                        testLabel + Arrays.toString(input),
                        () -> assertLoad(ReportHandler.class, input)
                )
        );

    }

    private <H> void assertLoad(Class<H> handlerType, String[] service) {
        final var beans = ctx.getBeansOfType(handlerType);
        assertAll(
                () -> assertTrue(beans.size() > 0),
                () -> assertTrue(beans.containsKey(service[0])),
                () -> assertInstanceOf(Class.forName(service[1]), beans.get(service[0]))
        );
    }

}
