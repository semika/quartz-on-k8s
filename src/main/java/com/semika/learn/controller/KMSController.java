package com.semika.learn.controller;

import com.semika.learn.service.SampleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsAsyncClient;
import software.amazon.awssdk.services.kms.model.*;
import software.amazon.awssdk.services.kms.paginators.ListKeysPublisher;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/kms")
public class KMSController {

    @Autowired
    private SampleJobService sampleJobService;

    @GetMapping("/list-keys")
    public ResponseEntity<String> listAllKeys() {
        KmsAsyncClient kmsAsyncClient = KmsAsyncClient.builder()
                .build();
        ListKeysRequest listKeysRequest = ListKeysRequest.builder()
                .limit(15)
                .build();

        /*
         * The `subscribe` method is required when using paginator methods in the AWS SDK
         * because paginator methods return an instance of a `ListKeysPublisher`, which is
         * based on a reactive stream. This allows asynchronous retrieval of paginated
         * results as they become available. By subscribing to the stream, we can process
         * each page of results as they are emitted.
         */
        ListKeysPublisher keysPublisher = kmsAsyncClient.listKeysPaginator(listKeysRequest);
        CompletableFuture<Void> future = keysPublisher
                .subscribe(r -> r.keys().forEach(key ->
                        System.out.println("The key ARN is: " + key.keyArn() + ". The key Id is: " + key.keyId())))
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        System.err.println("Error occurred: " + exception.getMessage());
                    } else {
                        System.out.println("Successfully listed all keys.");
                    }
                });

        try {
            future.join();
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Failed to list keys: " + e.getMessage());
            return new ResponseEntity<>("Failed", HttpStatus.OK);
        }
    }

    @GetMapping("/encrypt")
    public ResponseEntity<String> encryptDataAsync(@RequestParam String text) throws ExecutionException, InterruptedException {
        String keyId = "dd8971e3-7de9-4726-a1ee-1b1063c342f2";
        SdkBytes myBytes = SdkBytes.fromUtf8String(text);
        EncryptRequest encryptRequest = EncryptRequest.builder()
                .keyId(keyId)
                .plaintext(myBytes)
                .build();

        KmsAsyncClient kmsAsyncClient = KmsAsyncClient.builder()
                .build();

        EncryptResponse responseFuture = kmsAsyncClient.encrypt(encryptRequest).get();

        return new ResponseEntity<>(responseFuture.ciphertextBlob().toString(), HttpStatus.OK);
    }

    @GetMapping("/decrypt")
    public ResponseEntity<String> decryptDataAsync(@RequestParam String text) throws ExecutionException, InterruptedException {
        String keyId = "dd8971e3-7de9-4726-a1ee-1b1063c342f2";
        SdkBytes myBytes = SdkBytes.fromUtf8String(text);
        EncryptRequest encryptRequest = EncryptRequest.builder()
                .keyId(keyId)
                .plaintext(myBytes)
                .build();
        KmsAsyncClient kmsAsyncClient = KmsAsyncClient.builder()
                .build();

        EncryptResponse responseFuture = kmsAsyncClient.encrypt(encryptRequest).get();//.toCompletableFuture();

        DecryptRequest decryptRequest = DecryptRequest.builder()
                .keyId(keyId)
                .ciphertextBlob(responseFuture.ciphertextBlob())
                .build();

        DecryptResponse decryptResponse = kmsAsyncClient.decrypt(decryptRequest).get();

        return new ResponseEntity<>(decryptResponse.plaintext().asString(StandardCharsets.UTF_8), HttpStatus.OK);
    }

}
