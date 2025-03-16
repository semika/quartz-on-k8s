package com.semika.learn.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@EnableAutoConfiguration
public class SqsAutoConfig {

    @Bean
    public SqsClient sqsClient() {
        SqsClient sqsClient = SqsClient.builder().region(Region.AP_SOUTHEAST_1)
                .build();
        return sqsClient;
    }
}
