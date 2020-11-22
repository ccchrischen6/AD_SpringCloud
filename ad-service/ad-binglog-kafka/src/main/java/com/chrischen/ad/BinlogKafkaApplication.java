package com.chrischen.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by Chris Chen
 */

@SpringBootApplication
@EnableEurekaClient
public class BinlogKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinlogKafkaApplication.class, args);
    }
}
