package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class TimeAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimeAppApplication.class, args);
    }
}

@RestController
@RequestMapping("/api")
class TimeController {
    @GetMapping("/time")
    public Map<String, String> getTime() {
        Map<String, String> response = new HashMap<>();
        response.put("time", ZonedDateTime.now().toString());
        return response;
    }
}