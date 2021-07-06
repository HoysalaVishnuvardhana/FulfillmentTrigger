package com.walmart.fulfillmentutiltrigger.Runnable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.fulfillmentutiltrigger.Callback.ProducerDataCallback;
import com.walmart.fulfillmentutiltrigger.CommonUtil.ConverterUtil;
import com.walmart.fulfillmentutiltrigger.CommonUtil.LoggerUtil;
import com.walmart.fulfillmentutiltrigger.Constants.CommonConstants;
import com.walmart.fulfillmentutiltrigger.elastic.ElasticQueryExecutor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class FrProduceDataRunnable implements Runnable {
    private Environment environment;
    private KafkaProducer<String, String> kafkaProducer;
    private LoggerUtil loggerUtil;
    private String producerTopic;
    private Map<String, Object> frMap;
    private ConverterUtil converterUtil;
    private int threads;
    private ElasticQueryExecutor elasticQueryExecutor;

    public FrProduceDataRunnable(Environment environment, KafkaProducer<String, String> kafkaProducer, LoggerUtil loggerUtil,
                                 ConverterUtil converterUtil, Map<String, Object> frMap, int threads, String producerTopic, ElasticQueryExecutor elasticQueryExecutor) {
        this.environment = environment;
        this.producerTopic = producerTopic;
        this.kafkaProducer = kafkaProducer;
        this.loggerUtil = loggerUtil;
        this.frMap = frMap;
        this.converterUtil = converterUtil;
        this.threads = threads;
        this.elasticQueryExecutor = elasticQueryExecutor;
    }

    @Override
    public void run() {
        Random random = new Random();
//        while (true) {
////            StringBuilder orderId = new StringBuilder(String.valueOf(System.currentTimeMillis()));
////            orderId.append(random.nextInt(100));
//            String orderId = UUID.randomUUID().toString();
//            Object obj = frMap.get("eventPayload");
//            CommonConstants.TOTAL_MESSAGE_SENT_COUNT.getAndIncrement();
//            //loggerUtil.info(" Total messages sent till now is : "+ totalMessagesRead);
//            ObjectMapper m = new ObjectMapper();
//            Map<String, Object> props = m.convertValue(obj, Map.class);
//            props.put("sellerOrderId", orderId);
//            Map<String, Object> map = new HashMap<>();
//            map.put("eventPayload", props);
//            map.put("eventId", UUID.randomUUID().toString());
//            map.put("eventName", "FULFILLMENT_REQUEST_CREATE");
//            map.put("eventSource", "OMS-SAMS");
//            try {
//                this.produceRequest(orderId.toString(), new ObjectMapper().writeValueAsString(map));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }

        while (true) {
//            StringBuilder orderId = new StringBuilder(String.valueOf(System.currentTimeMillis()));
//            orderId.append(random.nextInt(100));
            String orderId = UUID.randomUUID().toString();
            Object obj = frMap.get("eventPayload");

            List<String> purchaseOrders = elasticQueryExecutor.getDataFromES();

            for (String po : purchaseOrders) {
                CommonConstants.TOTAL_MESSAGE_SENT_COUNT.getAndIncrement();
                //loggerUtil.info(" Total messages sent till now is : "+ totalMessagesRead);
                ObjectMapper m = new ObjectMapper();
                Map<String, Object> props = m.convertValue(obj, Map.class);
                props.put("purchaseOrderNo", po);
                Map<String, Object> map = new HashMap<>();
                map.put("eventPayload", props);
                map.put("eventId", UUID.randomUUID().toString());
                map.put("eventName", "ORDER_STATUS_UPDATE");
                map.put("eventSource", "EGLS");
                map.put("eventTime", new Date());
                try {
                    this.produceRequest(orderId.toString(), new ObjectMapper().writeValueAsString(map));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void produceRequest(String key, String message) {
        loggerUtil.info(" message to be published " + key);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(producerTopic, key, message);
        this.kafkaProducer.send(producerRecord, new ProducerDataCallback(key, message, loggerUtil));
    }
}
