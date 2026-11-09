package com.semika.learn.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semika.learn.model.ProductDto;
import com.semika.learn.model.ProductResponseDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ProductProcessService {

    public String parseJson() throws IOException {

        int numOfThreds = 1;
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreds); // 3 threads

        for (int i = 1; i <= numOfThreds; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    System.out.println("Task " + taskId + " is running in " + Thread.currentThread().getName());
                    ClassPathResource resource = new ClassPathResource("products2.json");
                    File jsonFile = null; // Will throw IOException if not found
                    jsonFile = resource.getFile();
                    ObjectMapper mapper = new ObjectMapper();
                    ProductResponseDto productResponseDto = mapper.readValue(jsonFile, ProductResponseDto.class);

                    List<ProductDto> productDtoList = new ArrayList<>(productResponseDto.getProducts().size());

                    productResponseDto.getProducts().stream().map(productDto -> productDtoList.add(productDto));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }

        executor.shutdown(); // Don't forget to shutdown

        return "success";
    }

    public String parseJsonAsStream() throws IOException {

        try {
            //Creating root level object only with header level fields.
            ClassPathResource resource = new ClassPathResource("products2.json");
            File jsonFile = resource.getFile();
            ObjectMapper mapper = new ObjectMapper();
            ProductResponseDto productResponseDto = mapper.readValue(jsonFile, ProductResponseDto.class);

            //Get the JSON response as a string. This will be API response as a string
            //when processing API response.
            String jsonResponse = Files.readString(Path.of(jsonFile.getPath()));
            //Extract the json string segment which has array of json objects.
            //This will be different according to response format.
            String productsArray = jsonResponse.substring(jsonResponse.indexOf("["), jsonResponse.lastIndexOf("]") + 1);
            //Create a temp file
            Path tempFile = Files.createTempFile("products", ".json");
            // Write json string to the temp file
            Files.writeString(tempFile, productsArray);
            // Read the file as stream
            parseJsonInChunk(tempFile.toAbsolutePath().toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    /**
     * To read a json file and convert into java objects one by one.
     * Does not parse all at once into objects.
     *
     * example json data payload format : [{}, {}, {}.....]
     * @return
     * @throws IOException
     */
    private String parseJsonInChunk(String tempFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(tempFilePath);
        //parse only 10 objects at a time
        int batchSize = 10;
        List<ProductDto> productDtoList = new ArrayList<>(batchSize);
        InputStream input = new FileInputStream(jsonFile);
        try {
            MappingIterator<ProductDto> it = mapper.readerFor(ProductDto.class).readValues(input);
            int index   = 0;
            int batchNo = 0;
            while (it.hasNext()) {
                ProductDto obj = it.next();
                // Process each item
                if (index < batchSize) {
                    productDtoList.add(obj);
                    if (!it.hasNext()) { //last object
                        processList(productDtoList, batchNo++); //processing last batch
                        index = 0;
                        productDtoList.clear();
                    }
                } else {
                    processList(productDtoList, batchNo++);
                    // clear for next batch
                    index = 0;
                    productDtoList.clear();
                }
                index++;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            input.close();
        }
        return "success";
    }

    private void processList(List<ProductDto> productDtoList, int batchNo) throws InterruptedException {
        System.out.println("Start Processing Batch #" + batchNo);
        Thread.sleep(1000);
        System.out.println("Ended Processing Batch #" + batchNo + "\n");
    }

    public String parseJsonInToken() throws IOException {
        JsonFactory factory = new JsonFactory();
        ClassPathResource resource = new ClassPathResource("products.json");
        File jsonFile = resource.getFile();
        try (JsonParser parser = factory.createParser(jsonFile)) {
            while (!parser.isClosed()) {
                JsonToken token = parser.nextToken();
                if (parser.currentToken().asString().equals("products")) {
                    System.out.println(token + ":" + parser.getValueAsString());
                }
                // Process tokens incrementally
            }
        }
        return "success";
    }

    public String extractCategoryNameAsStream() throws IOException {
        ClassPathResource resource = new ClassPathResource("products.json");
        File jsonFile = resource.getFile();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(jsonFile);
        Set<String> uniqueCategories = new HashSet<>();
        // Start parsing
        while (!parser.isClosed()) {
            JsonToken token = parser.nextToken();
            if (token == JsonToken.FIELD_NAME && "product_id".equals(parser.getCurrentName())) {
                parser.nextToken(); // "product_id" value token
                parser.nextToken(); // "company_id" field token
                parser.nextToken(); // "company_id" value token
                parser.nextToken(); // "store_id" field token
                parser.nextToken(); // "store_id" value token
                parser.nextToken(); // "category_id" field token
                //System.out.println("After Store ID Value" + parser.getCurrentName());
                if (token == JsonToken.FIELD_NAME && "category_id".equals(parser.getCurrentName())) {
                    parser.nextToken(); // move to value
                    String category = parser.getValueAsString();
                    uniqueCategories.add(category);
                }
            }
        }
        parser.close();
        // Output unique category names
        uniqueCategories.forEach(System.out::println);
        return "success";
    }
}
