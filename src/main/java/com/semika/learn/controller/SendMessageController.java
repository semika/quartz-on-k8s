package com.semika.learn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@RestController("/")
public class SendMessageController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping
    public ResponseEntity<String> hellow() {
        return new ResponseEntity<>("Hellow", HttpStatus.OK);
    }

    @PostMapping("/send-message")
    public ResponseEntity<String> sendSqsMessage(@RequestParam String queueName, @RequestParam  String message) {
        SqsClient sqsClient = SqsClient.builder().region(Region.AP_SOUTHEAST_1)
                .build();
        try {
            sendMessage(sqsClient, queueName, message);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Sending message failed", e);
            return new ResponseEntity<>("Error", HttpStatus.OK);
        } finally {
            sqsClient.close();
        }
    }

    private void sendMessage(SqsClient sqsClient, String queueName, String message) {
        CreateQueueRequest request = CreateQueueRequest.builder()
                .queueName(queueName)
                .build();
        sqsClient.createQueue(request);

        GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();

        String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .delaySeconds(5)
                .build();

        sqsClient.sendMessage(sendMsgRequest);
    }

    @GetMapping("/receive-message")
    public ResponseEntity<String> receiveMessage(@RequestParam String queueName) {
        SqsClient sqsClient = SqsClient.builder().region(Region.AP_SOUTHEAST_1)
                .build();
        try {
            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();

            String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();

            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(5)
                    .build();

            List<Message> messageList = sqsClient.receiveMessage(receiveMessageRequest).messages();
            messageList.forEach(message -> {
                System.out.println("Body of the message : " + message.body());

                //delete message
                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build();
                sqsClient.deleteMessage(deleteMessageRequest);
            });
            return new ResponseEntity("Success", HttpStatus.OK);

        } catch (SqsException e) {
            logger.error("Error", e);
            return new ResponseEntity<>("Error", HttpStatus.OK);
        } finally {
            sqsClient.close();
        }
    }

    @PostMapping("/send-sns")
    public ResponseEntity<String> sendSns(@RequestParam  String message) {
        String topicArn = "arn:aws:sns:ap-southeast-1:762002331286:thapro";
        SnsClient snsClient = SnsClient.builder()
                .region(Region.AP_SOUTHEAST_1)
                .build();
        try {
            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .topicArn(topicArn)
                    .build();

            PublishResponse result = snsClient.publish(request);
            return new ResponseEntity<>(result.messageId() + " Message sent. Status is " + result.sdkHttpResponse().statusCode(),
                    HttpStatus.OK);

        } catch (SnsException e) {
            logger.error("Error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

}
