package com.hpc.relearn.demo.rest.secured;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/secured/greet")
public class SecureGreetingsResource {

    @GetMapping
    public ResponseEntity<Object> greet(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(Map.of("message", String.format("Hello %s!", jwt.getClaimAsString("preferred_username"))));
    }
}
