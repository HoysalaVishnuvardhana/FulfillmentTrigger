package com.walmart.fulfillmentutiltrigger.Scheduler;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import com.walmart.fulfillmentutiltrigger.Service.ProducerDataService;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn("elasticQueryExecutor")
public class ProduceFrDataScheduler {
    @Value("${is.fr.produceData.flag}")
    private boolean reproduceData;

    @Autowired
    private ProducerDataService dataloggerService;

    @PostConstruct
    private void FrReproduceData() throws Exception{
        if (reproduceData)
            dataloggerService.frReproduceData();
    }

//    @PostConstruct
//    private void StatusreproduceData() throws Exception{
//        if (reproduceData)
//            dataloggerService.frReproduceData();
//    }

}
