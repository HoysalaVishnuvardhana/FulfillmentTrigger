package com.walmart.fulfillmentutiltrigger.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ProducerConfiguration {
    @Autowired
    private Environment env;

    @Value("${kafka.threads}")
    private Integer threadCount;

    @Bean("producerProperties")
    public Properties producerProperties() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", env.getProperty("kafka.producer.bootstrap.servers"));
        properties.put("acks", env.getProperty("kafka.producer.acks"));
        properties.put("retries", env.getProperty("kafka.producer.retries"));
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("retry.backoff.ms", env.getProperty("kafka.producer.retry.backoff.ms"));
        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.put("linger.ms", env.getProperty("kafka.producer.linger.ms"));
        properties.put("max.in.flight.requests.per.connection", env.getProperty("kafka.producer.max.in.flight.requests.per.connection"));
        properties.put("batch.size", env.getProperty("kafka.producer.batch.size"));
        properties.put("buffer.memory", env.getProperty("kafka.producer.buffer.memory"));
        properties.put("block.on.buffer.full", env.getProperty("kafka.producer.block.on.buffer.full"));
        properties.put("compression.type", env.getProperty("kafka.producer.compression.type"));
        properties.setProperty("serializer.class", "kafka.serializer.StringEncoder");
        properties.setProperty("request.required.acks", "1");
        properties.setProperty("max.request.size", "1600000");
        properties.setProperty("security.protocol", "SSL");
        properties.setProperty("ssl.truststore.password", "truststore@123");
        properties.setProperty("ssl.truststore.location", "/Users/s0k06iv/Downloads/SSLCertificate/truststore.jks");
        properties.setProperty("ssl.keystore.password", "keystore@123");
        properties.setProperty("ssl.keystore.location", "/Users/s0k06iv/Downloads/SSLCertificate/keystore.jks");
        properties.setProperty("ssl.key.password", "keystore@123");
        properties.setProperty("ssl.endpoint.identification.algorithm","");
        return properties;
    }

    @Autowired
    @Bean("kafkaProducers")
    public List<KafkaProducer<String, String>> kafkaProducers(@Qualifier("producerProperties") Properties properties) {
        List<KafkaProducer<String, String>> kafkaProducers = new ArrayList<KafkaProducer<String, String>>(threadCount);
        for (int i = 0; i < threadCount; i++)
            kafkaProducers.add(new KafkaProducer<String, String>(properties));
        return kafkaProducers;
    }



}
