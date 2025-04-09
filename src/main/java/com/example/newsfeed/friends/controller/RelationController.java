package com.example.newsfeed.friends.controller;

import com.example.newsfeed.friends.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relations")
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;

    @PostMapping
    public ResponseEntity<?> getFriends(@RequestParam String userEmail) {
        List<FriendResponseDto> friends = relationService.getFriends(userEmail);
        return ResponseEntity.ok(friends);
    }
}
