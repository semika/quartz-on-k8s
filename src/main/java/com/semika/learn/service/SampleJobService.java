package com.semika.learn.service;

import com.semika.learn.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SampleJobService {

    public static final long EXECUTION_TIME = 5000L;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicInteger count = new AtomicInteger();

    public void executeSampleJob() throws InterruptedException {
        logger.info("The sample job has begun...");
        createObjects();
        TimeUnit.MINUTES.sleep(1);
        logger.info("The sample job has ended...");
    }

    private void createObjects() {

        List empList = new ArrayList(1000000);
        for (int i = 0; i < 1000000; i++) {
            Employee employee = new Employee();
            employee.setId("3434er343er3rerergfgfg3434");
            employee.setFirstName("Semika");
            employee.setLastName("Siriwardana");
            employee.setAge(45);
            employee.setAddress("We manage the infrastructure, hardware and network. While you simply enjoy the weather forecast data and leave rest on us.");
            empList.add(employee);
            //logger.info("Employee" + employee.toString());
        }
        empList = null;
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
