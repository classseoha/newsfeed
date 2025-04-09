package com.example.newsfeed.friends.controller;

import com.example.newsfeed.friends.dto.RelationRequestDto;
import com.example.newsfeed.friends.dto.RelationResponseDto;
import com.example.newsfeed.friends.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relations")
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;

    @PostMapping
    public ResponseEntity<List<RelationResponseDto>> getFriends(@RequestBody RelationRequestDto relationRequestDto) {
        List<RelationResponseDto> friends = relationService.getFriends(relationRequestDto.getEmail());
        return ResponseEntity.ok(friends);
    }

    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@RequestBody RelationRequestDto relationRequestDto) {
        relationService.sendFriendRequest(relationRequestDto.getEmail(), relationRequestDto.getTargetEmail());
        return ResponseEntity.ok("친구 요청이 전송되었습니다.");
    }
}
