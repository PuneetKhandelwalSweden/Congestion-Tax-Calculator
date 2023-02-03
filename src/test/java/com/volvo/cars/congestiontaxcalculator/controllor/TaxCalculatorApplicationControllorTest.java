package com.volvo.cars.congestiontaxcalculator.controllor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.volvo.cars.congestiontaxcalculator.model.TaxCalculatorResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TaxCalculatorApplicationControllorTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    //Testing the free time entry - 2013-01-14 21:00:00
    @Test
    public void testFreeTime() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-01-14 21:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Testing the Public holiday time entry - 2013-01-01
    @Test
    public void testHoliday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-01-01 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
  //Testing the before public holiday time entry - 2013-03-27, Mar 28 is public holiday
    @Test
    public void testBeforeHoliday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-03-27 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

  //Testing the Sunday time entry - 2013-01-06 SUNDAY
    @Test
    public void testSunday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-01-06 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

  //Testing the Saturday time entry - 2013-01-05 Saturday
    @Test
    public void testSaturday() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-01-05 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

  //Testing the workingday time entry - 2013-01-07 10:00:00 Saturday
    @Test
    public void testWorkingday10AM() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(8, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

  //Testing the workingday time entry - 2013-01-07 08:30:00 Saturday
    @Test
    public void testWorkingday0830AM() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 08:30:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(8, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Testing the workingday time entry - 2013-01-07 16:59:00 Saturday
    @Test
    public void testWorkingday1659AM() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 16:59:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(18, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Testing the workingday time entry - 2013-01-07 16:59:59 Saturday
    @Test
    public void testWorkingday165959AM() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 16:59:59\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(18, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Testing the two entries in 60min 2013-01-07 16:30:59, 2013-01-07 16:59:59
    @Test
    public void testTwoEntriesIn60Min() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 16:30:59\",\"2013-01-07 16:59:59\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(18, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

  //Testing the two entries in 60min 2013-01-07 15:30:59, 2013-01-07 16:59:59
    @Test
    public void testTwoEntriesNotIn60Min() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-01-07 15:30:59\",\"2013-01-07 16:59:59\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(36, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //Testing The maximum amount 60SEK
   @Test
    public void testMaximumAmount60SEK() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": ["
                + "\"2013-02-08 06:27:00\",\"2013-02-08 06:20:27\","
                + "\"2013-02-08 14:35:00\",\"2013-02-08 15:29:00\","
                + "\"2013-02-08 15:47:00\",\"2013-02-08 16:01:00\","
                + "\"2013-02-08 16:48:00\",\"2013-02-08 17:49:00\","
                + "\"2013-02-08 18:29:00\",\"2013-02-08 18:35:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(60, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    //Testing the Tax Exempt vehicles - 2013-01-07 10:00:00
    @Test
    public void testTaxExemptVehicles() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Emergency\",\"dates\": [\"2013-01-07 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    //Testing the Month of July - 2013-07-01 10:00:00
    @Test
    public void testMonthOfJuly() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-07-01 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
  //Testing Bad vachicle name
    @Test
    public void testBadVechileName() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car1\",\"dates\": [\"2013-07-01 10:00:00\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    //Testing Bad Date Format Expected 2013-07-01 10:00:00 but provided the 2013-07-01
    @Test
    public void testBadDateFormat() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Gothenburg\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-07-01\"]}", headers);
        ResponseEntity<TaxCalculatorResponse> response = restTemplate
                .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
        assertEquals(0, response.getBody().getTax());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

  //Testing city which is yet to onboarded
  @Test
  public void testStockholm() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>("{\"city\": \"Stockholm\", \"vehicle_type\": \"Car\",\"dates\": [\"2013-07-01\"]}", headers);
    ResponseEntity<TaxCalculatorResponse> response = restTemplate
        .postForEntity(new URL("http://localhost:" + port + "/tax/calculator/sweden").toString(), request, TaxCalculatorResponse.class);
    assertEquals(0, response.getBody().getTax());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
