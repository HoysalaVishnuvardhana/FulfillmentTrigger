package com.walmart.fulfillmentutiltrigger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.walmart.fulfillmentutiltrigger"})
public class FulfillmentUtilTriggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FulfillmentUtilTriggerApplication.class, args);
	}

}
