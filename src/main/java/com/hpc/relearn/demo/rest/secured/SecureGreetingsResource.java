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

    public static final String PREFERRED_USERNAME = "preferred_username";
    public static final String EMAIL = "email";

    @GetMapping
    public ResponseEntity<Object> greet(@AuthenticationPrincipal Jwt jwt) {
        var username = jwt.getClaimAsString(PREFERRED_USERNAME);
        var email = jwt.getClaimAsString(EMAIL);
        return ResponseEntity.ok(Map.of("greetings", String.format("Hello %s!", username), EMAIL, email));
    }
}
