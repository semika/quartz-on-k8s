package com.semika.learn.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.semika.learn.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamRequest;
import software.amazon.awssdk.services.kinesis.model.DescribeStreamResponse;
import software.amazon.awssdk.services.kinesis.model.KinesisException;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

@Service
public class KinesisProducerService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void executeJob() throws JsonProcessingException {
        putRecord("my-first-kinesis-stream");
    }

    private void putRecord(String dataStreamName) throws JsonProcessingException {
        KinesisClient kinesisClient = KinesisClient.builder()
                .region(Region.AP_SOUTHEAST_1)
                .build();
        validateStream(kinesisClient, dataStreamName);
        putOrderRecord(kinesisClient, dataStreamName);
    }

    private void validateStream(KinesisClient kinesisClient, String streamName) {
        try {
            DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder()
                    .streamName(streamName)
                    .build();

            DescribeStreamResponse describeStreamResponse = kinesisClient.describeStream(describeStreamRequest);

            if (!describeStreamResponse.streamDescription().streamStatus().toString().equals("ACTIVE")) {
                logger.error("Stream " + streamName + " is not active. Please wait a few moments and try again.");
            }

        } catch (KinesisException e) {
            logger.error("Error found while describing the stream " + streamName, e);
        }
    }

    private void putOrderRecord(KinesisClient kinesisClient, String streamName) throws JsonProcessingException {
        Order order = new Order();
        order.setId("ertr3445rtrt");
        order.setOrderStatus("COMPLETED");
        order.setOrderTotal(12.34);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(order);
        byte[] bytes = json.getBytes();

        PutRecordRequest request = PutRecordRequest.builder()
                .partitionKey(order.getId()) // We use the ticker symbol as the partition key, explained in
                // the Supplemental Information section below.
                .streamName(streamName)
                .data(SdkBytes.fromByteArray(bytes))
                .build();

        try {
            kinesisClient.putRecord(request);
        } catch (KinesisException e) {
            logger.error("Error", e);
        }
    }
}
