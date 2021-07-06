package com.walmart.fulfillmentutiltrigger.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.walmart.fulfillmentutiltrigger.CommonUtil.ConverterUtil;
import com.walmart.fulfillmentutiltrigger.CommonUtil.LoggerUtil;
import com.walmart.fulfillmentutiltrigger.Constants.CommonConstants;
import com.walmart.fulfillmentutiltrigger.Mapper.FrEventMapper;
import com.walmart.fulfillmentutiltrigger.Runnable.FrProduceDataRunnable;

@Service
public class ProducerDataService {
    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("kafkaProducers")
    private List<KafkaProducer<String, String>> kafkaProducers;

    @Autowired
    private FrEventMapper frEventMapper;

    @Autowired
    private LoggerUtil loggerUtil;

    @Autowired
    private ConverterUtil converterUtil;

    @Value("${kafka.threads}")
    private Integer threadCount;

    @Value("${fixedDelay.in.milliseconds}")
    private Integer fixedDelay;

    @Value("${kafka.producer.topic}")
    private String producerTopic;

    private static int schedulerCount=0;

    public void frReproduceData() throws Exception {
        Map<String, Object> frMap= frEventMapper.getFrData();
        Set<String> sentEvents = Collections.synchronizedSet(new HashSet<>());
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++)
            executorService.execute(new FrProduceDataRunnable(env, kafkaProducers.get(i), loggerUtil, converterUtil, frMap, threadCount, producerTopic));
    }

    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void scheduledTask() throws Exception{
        schedulerCount++;
        long totalMessagesSent = CommonConstants.TOTAL_MESSAGE_SENT_COUNT.get();
        loggerUtil.info(" No of events produced in  " + fixedDelay*schedulerCount + " milliseconds is: "
                + totalMessagesSent + " by " + threadCount + " threads ");
    }
}
