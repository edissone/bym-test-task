package com.edissone.bymtask.context.config;

import co.elastic.clients.elasticsearch.nodes.Client;
import com.edissone.bymtask.order.dao.elastic.repository.OrderElasticRepository;
import lombok.NonNull;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.support.HttpHeaders;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = {OrderElasticRepository.class})
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    @Value("${spring.elasticsearch.url}")
    private String url;

    @Override
    public @NonNull ClientConfiguration clientConfiguration() {
        final var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return ClientConfiguration.builder()
                .connectedTo(url)
                .withDefaultHeaders(headers)
                .build();
    }
}
