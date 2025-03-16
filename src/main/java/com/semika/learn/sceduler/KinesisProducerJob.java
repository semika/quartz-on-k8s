package com.semika.learn.sceduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.semika.learn.service.KinesisProducerService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class KinesisProducerJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KinesisProducerService kinesisProducerService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Starting kinesis producer job....");
        try {
            kinesisProducerService.executeJob();
        } catch (JsonProcessingException e) {
            logger.error("Jon execution failed" , e);
        }
        logger.info("Ending kinesis producer job ....");
    }
}
