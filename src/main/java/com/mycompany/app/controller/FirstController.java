package com.mycompany.app.controller;

import com.mycompany.app.model.RandomTestEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {

    @RequestMapping("/test")
    public ResponseEntity<?> test() {
        HttpHeaders resp_headers = new HttpHeaders();
        return new ResponseEntity<String>("the testing route is working", resp_headers, HttpStatus.OK);
    }

    // to return json object
    @RequestMapping("/testEntity")
    public ResponseEntity<?> testEntity() {
        HttpHeaders resp_headers = new HttpHeaders();
        RandomTestEntity test = new RandomTestEntity("testing string", 420);
        return new ResponseEntity<RandomTestEntity>(test, resp_headers, HttpStatus.OK);
    }
}