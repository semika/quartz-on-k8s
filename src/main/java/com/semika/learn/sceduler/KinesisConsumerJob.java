package com.semika.learn.sceduler;

import com.semika.learn.service.KinesisConsumerService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class KinesisConsumerJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KinesisConsumerService kinesisConsumerService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Starting kinesis consumer job ...");
        kinesisConsumerService.executeJob();
        logger.info("Ending kinesis consumer job...");
    }
}
