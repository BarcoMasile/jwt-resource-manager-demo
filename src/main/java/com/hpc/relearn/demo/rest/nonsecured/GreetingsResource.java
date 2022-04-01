package com.hpc.relearn.demo.rest.nonsecured;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/public/greet")
public class GreetingsResource {

    @GetMapping
    public ResponseEntity<Object> greet(@RequestParam String name) {
        return ResponseEntity.ok(Map.of("message", String.format("Hello %s!", name)));
    }
}
