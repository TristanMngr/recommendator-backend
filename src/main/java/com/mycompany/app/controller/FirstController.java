package com.mycompany.app.controller;

import com.mycompany.app.model.Module;
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
    @RequestMapping("/module")
    public ResponseEntity<?> testEntity() {
        HttpHeaders resp_headers = new HttpHeaders();
        Module java = new Module(1, "Java avancé", "cours d'algorithmique en java");
        return new ResponseEntity<Module>(java, resp_headers, HttpStatus.OK);
    }
}