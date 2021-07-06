package com.walmart.fulfillmentutiltrigger.Runnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.core.env.Environment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.fulfillmentutiltrigger.Callback.ProducerDataCallback;
import com.walmart.fulfillmentutiltrigger.CommonUtil.ConverterUtil;
import com.walmart.fulfillmentutiltrigger.CommonUtil.LoggerUtil;
import com.walmart.fulfillmentutiltrigger.Constants.CommonConstants;

public class FrProduceDataRunnable implements Runnable {
    private Environment environment;
    private KafkaProducer<String, String> kafkaProducer;
    private LoggerUtil loggerUtil;
    private String producerTopic;
    private Map<String, Object> frMap;
    private ConverterUtil converterUtil;
    private int threads;

    public FrProduceDataRunnable(Environment environment, KafkaProducer<String, String> kafkaProducer, LoggerUtil loggerUtil,
                                 ConverterUtil converterUtil, Map<String, Object> frMap, int threads, String producerTopic) {
        this.environment = environment;
        this.producerTopic = producerTopic;
        this.kafkaProducer = kafkaProducer;
        this.loggerUtil = loggerUtil;
        this.frMap = frMap;
        this.converterUtil = converterUtil;
        this.threads = threads;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (true) {
//            StringBuilder orderId = new StringBuilder(String.valueOf(System.currentTimeMillis()));
//            orderId.append(random.nextInt(100));
            String orderId = UUID.randomUUID().toString();
            Object obj = frMap.get("eventPayload");
            CommonConstants.TOTAL_MESSAGE_SENT_COUNT.getAndIncrement();
            //loggerUtil.info(" Total messages sent till now is : "+ totalMessagesRead);
            ObjectMapper m = new ObjectMapper();
            Map<String, Object> props = m.convertValue(obj, Map.class);
            props.put("sellerOrderId", orderId);
            Map<String, Object> map = new HashMap<>();
            map.put("eventPayload", props);
            map.put("eventId", UUID.randomUUID().toString());
            map.put("eventName", "FULFILLMENT_REQUEST_CREATE");
            map.put("eventSource", "OMS-SAMS");
            try {
                this.produceRequest(orderId.toString(), new ObjectMapper().writeValueAsString(map));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private void produceRequest(String key, String message) {
        loggerUtil.info(" message to be published " + key);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(producerTopic, key, message);
        this.kafkaProducer.send(producerRecord, new ProducerDataCallback(key, message, loggerUtil));
    }
}
