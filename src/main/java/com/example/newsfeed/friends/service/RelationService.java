package com.example.newsfeed.friends.service;

import com.example.newsfeed.entity.Relation;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.friends.dto.RelationResponseDto;
import com.example.newsfeed.friends.repository.RelationRepository;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelationService {

    private final RelationRepository relationRepository;
    private final UserRepository userRepository;

    public List<RelationResponseDto> getFriends(String userEmail) {
        User user = userRepository.findByIdOrElseThrow(userEmail);

        List<Relation> sentRequests = relationRepository.findByFollowingEmail_Email(userEmail);
        List<Relation> receivedRequests = relationRepository.findByFollowedEmail_Email(userEmail);

        return sentRequests.stream()
                .filter(req -> receivedRequests.stream().anyMatch(
                        r -> r.getFollowingEmail().getEmail().equals(req.getFollowedEmail().getEmail())
                ))
                .map(Relation::getFollowedEmail)
                .map(RelationResponseDto::from)
                .collect(Collectors.toList());
    }
    public void sendFriendRequest(String fromEmail, String toEmail) {

        if(fromEmail.equals(toEmail)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User fromUser = userRepository.findByIdOrElseThrow(fromEmail);
        User toUser = userRepository.findByIdOrElseThrow(toEmail);

        if(relationRepository.existsByFollowingEmailAndFollowedEmail(fromUser, toUser)) {
            throw new IllegalArgumentException("이미 친구 요청을 보냈습니다.");
        }


        Relation relation = new Relation();
        relation.setFollowingEmail(fromUser);
        relation.setFollowedEmail(toUser);
        relationRepository.save(relation);
    }
}
