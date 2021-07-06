package com.walmart.fulfillmentutiltrigger.context;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by p0k00df on 06/07/21
 **/
@Configuration
public class ElasticSearchContextInitializer {

    private String EsHost = "es-data-1-1018550503.prod.us.walmart.net:9200,es-data-2-1018550506.prod.us.walmart.net:9200,es-data-3-1018550509.prod.us.walmart.net:9200,es-data-4-1018550512.prod.us.walmart.net:9200,es-data-5-1018550515.prod.us.walmart.net:9200,es-data-6-1018550518.prod.us.walmart.net:9200";

    private int EsPort = 9200;

    private String EsUserName = "SVCFMSAPP_SECONDARY";

    private String EsPassword = "{L3)vyK<5&79t)^u$|=R";

    private int connectionTimeout = 15000;

    private int socketTimeout = 15000;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        String[] esHostsArray = EsHost.split(",");
        HttpHost[] httpHosts = new HttpHost[esHostsArray.length];

        for(int i = 0; i < esHostsArray.length; ++i) {
            String host = esHostsArray[i].split(":")[0];
            int port = Integer.parseInt(esHostsArray[i].split(":")[1]);
            httpHosts[i] = new HttpHost(host, port, "https");
        }

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(EsUserName, EsPassword));
        RestClientBuilder builder = RestClient.builder(httpHosts).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        }).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                return requestConfigBuilder.setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout);
            }
        });
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
