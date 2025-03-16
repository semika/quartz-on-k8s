package com.semika.learn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.*;
import software.amazon.awssdk.services.kinesis.model.Record;

import java.util.ArrayList;
import java.util.List;

@Service
public class KinesisConsumerService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void executeJob() {
        readRecord("my-first-kinesis-stream");
    }

    private void readRecord(String streamName) {
        KinesisClient kinesisClient = KinesisClient.builder()
                .region(Region.AP_SOUTHEAST_1)
                .build();

        String shardIterator;
        String lastShardId = null;
        DescribeStreamRequest describeStreamRequest = DescribeStreamRequest.builder()
                .streamName(streamName)
                .build();

        List<Shard> shards = new ArrayList<>();
        DescribeStreamResponse streamRes;
        do {
            streamRes = kinesisClient.describeStream(describeStreamRequest);
            shards.addAll(streamRes.streamDescription().shards());

            if (shards.size() > 0) {
                lastShardId = shards.get(shards.size() - 1).shardId();
            }
        } while (streamRes.streamDescription().hasMoreShards());

        GetShardIteratorRequest itReq = GetShardIteratorRequest.builder()
                .streamName(streamName)
                .shardIteratorType("TRIM_HORIZON")
                .shardId(lastShardId)
                .build();

        GetShardIteratorResponse shardIteratorResult = kinesisClient.getShardIterator(itReq);
        shardIterator = shardIteratorResult.shardIterator();

        // Continuously read data records from shard.
        List<Record> records;

        // Create new GetRecordsRequest with existing shardIterator.
        // Set maximum records to return to 1000.
        GetRecordsRequest recordsRequest = GetRecordsRequest.builder()
                .shardIterator(shardIterator)
                .limit(1000)
                .build();

        GetRecordsResponse result = kinesisClient.getRecords(recordsRequest);

        // Put result into record list. Result may be empty.
        records = result.records();

        // Print records
        for (Record record : records) {
            SdkBytes byteBuffer = record.data();
            logger.info(String.format("Seq No: %s - %s%n", record.sequenceNumber(), new String(byteBuffer.asByteArray())));
        }
    }
}
