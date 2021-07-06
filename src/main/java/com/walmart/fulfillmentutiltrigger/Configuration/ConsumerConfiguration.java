package com.walmart.fulfillmentutiltrigger.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

@Configuration
public class ConsumerConfiguration {
    @Value("${kafka.threads}")
    private Integer threadCount;

    @Autowired
    private Environment env;

    @Bean("consumerProperties")
    public Properties consumerProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", env.getProperty("kafka.consumer.bootstrap.servers"));
        properties.put("zookeeper.connect", env.getProperty("kafka.consumer.zookeeper"));
        properties.put("zookeeper.session.timeout.ms", env.getProperty("kafka.consumer.zookeeper.session.timeout.ms"));
        properties.put("zookeeper.sync.time.ms", env.getProperty("kafka.consumer.zookeeper.sync.time.ms"));
        properties.put("group.id", env.getProperty("kafka.consumer.group.name","util-six_" + UUID.randomUUID().toString()));
        properties.put("auto.offset.reset", env.getProperty("kafka.consumer.offset","largest"));
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", env.getProperty("kafka.consumer.auto.commit.interval.ms"));
        properties.put("fetch.wait.max.ms", env.getProperty("kafka.consumer.fetch.wait.max.ms"));
        properties.put("retry.backoff.ms", env.getProperty("kafka.consumer.retry.backoff.ms"));
        properties.put("rebalance.max.retries", env.getProperty("kafka.consumer.rebalance.max.retries"));
        return properties;
    }

}
