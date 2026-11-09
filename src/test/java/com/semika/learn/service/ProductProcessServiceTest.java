package com.semika.learn.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ProductProcessServiceTest {

    private final ProductProcessService service = new ProductProcessService();

    @Test
    void testParseJsonReturnsName() throws IOException {


        String result = service.parseJsonAsStream();
        //String result = service.extractCategoryNameAsStream();
        assertNotNull(result);
    }

//    @Test
//    void testParseJsonWithMissingNameReturnsEmptyString() {
//        String json = "{\"price\": 99.99}";
//        String result = service.parseJson(json);
//        assertEquals("", result); // Because JsonNode.asText() returns "" when the field is missing
//    }
//
//    @Test
//    void testParseJsonThrowsExceptionForInvalidJson() {
//        String invalidJson = "{name:\"Missing quotes\"}";
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            service.parseJson(invalidJson);
//        });
//        assertTrue(exception.getMessage().contains("Failed to parse JSON"));
//    }
}
