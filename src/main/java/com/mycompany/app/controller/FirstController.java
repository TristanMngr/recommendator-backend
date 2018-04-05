package com.mycompany.app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
public class FirstController {

    @RequestMapping("/test")
    public ResponseEntity<?> test() {
        HttpHeaders resp_headers = new HttpHeaders();
        return new ResponseEntity<String>("the testing route is working", resp_headers, HttpStatus.OK);
    }
}