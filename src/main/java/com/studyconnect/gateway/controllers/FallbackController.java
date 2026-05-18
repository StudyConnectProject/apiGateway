package com.studyconnect.gateway.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;
import java.util.Map;

@RestController
public class FallbackController {

    @RequestMapping("/fallback")
    public ResponseEntity<Map<String, Object>> fallback(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        Map<String, Object> body = Map.of(
                "error",     "Service temporarily unavailable",
                "message",   "The downstream service is not responding. Please try again later.",
                "timestamp", Instant.now().toString(),
                "path",      path
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }
}
