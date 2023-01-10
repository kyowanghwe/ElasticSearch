package com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;

@Configuration
public class ElasticSearchConfig {
	@Bean
	RestClient getRestClient() {
		Header[] headers = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json") };
		RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).setDefaultHeaders(headers)
				.setHttpClientConfigCallback(hc -> hc.addInterceptorLast((HttpResponseInterceptor) (response,
						context) -> response.addHeader(new BasicHeader("X-Elastic-Product", "Elasticsearch"))))
				.build();
		return restClient;
	}

	@Bean
	ElasticsearchTransport getElasticsearchTransport() {
		return new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
	}

	@Bean
	ElasticsearchClient getElasticsearchClient() {
		ElasticsearchClient client = new ElasticsearchClient(getElasticsearchTransport());
		return client;
	}
}