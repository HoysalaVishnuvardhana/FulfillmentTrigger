package com.walmart.fulfillmentutiltrigger.Callback;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import com.walmart.fulfillmentutiltrigger.CommonUtil.LoggerUtil;

public class ProducerDataCallback implements Callback {
    private String key;
    private String message;
    private LoggerUtil loggerUtil;

    public ProducerDataCallback(String key, String message, LoggerUtil loggerUtil) {
        this.key = key;
        this.message = message;
        this.loggerUtil = loggerUtil;
    }

    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
        if (null != exception)
            loggerUtil.error("Kafka error\t" + key + "\t" + message, exception);
    }
}
