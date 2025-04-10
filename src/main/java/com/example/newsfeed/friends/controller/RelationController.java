package com.example.newsfeed.friends.controller;

import com.example.newsfeed.friends.dto.RelationResponseDto;
import com.example.newsfeed.friends.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relations")
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;

    @GetMapping
    public ResponseEntity<List<RelationResponseDto>> getFriend() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<RelationResponseDto> friends = relationService.getFriends(email);

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> sendFriendRequest(@RequestParam String email) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String myEmail = authentication.getName();

        relationService.sendFriendRequest(myEmail, email);
        return ResponseEntity.ok("친구 요청이 전송되었습니다.");
    }
}
