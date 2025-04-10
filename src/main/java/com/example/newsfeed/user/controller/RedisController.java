package com.example.newsfeed.user.controller;

import com.example.newsfeed.user.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {


    private final RedisService redisService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam String key, @RequestParam String value) {
        redisService.save(key, value);
        return ResponseEntity.ok("Saved!");
    }

    @GetMapping("/get")
    public ResponseEntity<String> get(@RequestParam String key) {
        String value = redisService.get(key);
        return ResponseEntity.ok(value != null ? value : "Not Found");
    }
}
