package com.semika.learn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SampleJobService {

    public static final long EXECUTION_TIME = 5000L;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicInteger count = new AtomicInteger();

    public void executeSampleJob() {
        //readMessage();
        logger.info("The sample job has begun...");
    }

    private void readMessage() {
        SqsClient sqsClient = SqsClient.builder().region(Region.AP_SOUTHEAST_1)
                .build();
        try {
            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName("TEST")
                    .build();

            String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();

            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(5)
                    .build();

            List<Message> messageList = sqsClient.receiveMessage(receiveMessageRequest).messages();
            messageList.forEach(message -> {
                logger.info("Body of the message : " + message.body());
            });
        } catch (Exception e) {
            logger.error("Error while executing sample job", e);
        } finally {
            sqsClient.close();
            count.incrementAndGet();
            //logger.info("Sample job has finished...");
        }
    }
    public int getNumberOfInvocations() {
        return count.get();
    }
}
