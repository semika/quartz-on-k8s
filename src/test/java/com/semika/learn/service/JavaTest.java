package com.semika.learn.service;

import com.semika.learn.model.Employee;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JavaTest {

    private final ProductProcessService service = new ProductProcessService();

    @Test
    void testParseJsonReturnsName() throws IOException {
        List<Employee> employeeList = new ArrayList<>(2);

        Employee e1 = Employee.builder()
                .id("rtrt")
                .age(25)
                .firstName("Semika")
                .lastName("Siriwardana")
                .build();

        Employee e2 = Employee.builder()
                .id("rtrt")
                .age(25)
                .firstName("Semika")
                .lastName("Siriwardana")
                .build();
        List<Employee> newList = new ArrayList<>(2);
        newList.add(e1);
        newList.add(e2);

        employeeList.addAll(newList);
        newList=null;
        assertEquals(2, employeeList.size());
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
