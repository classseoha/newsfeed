package com.example.newsfeed.friends.service;

import com.example.newsfeed.entity.Relation;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.friends.dto.RelationResponseDto;
import com.example.newsfeed.friends.repository.RelationRepository;
import com.example.newsfeed.global.exception.CustomException;
import com.example.newsfeed.global.exception.ErrorCode;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelationService {

    private final RelationRepository relationRepository;
    private final UserRepository userRepository;

    public List<RelationResponseDto> getFriends(String userEmail) {
        User user = userRepository.findByIdOrElseThrow(userEmail);

        try {
            List<Relation> sentRequests = relationRepository.findByFollowingEmail_Email(userEmail);
            List<Relation> receivedRequests = relationRepository.findByFollowedEmail_Email(userEmail);

            List<RelationResponseDto> mutualFriends = sentRequests.stream()
                    .filter(req -> receivedRequests.stream().anyMatch(
                            r -> r.getFollowingEmail().getEmail().equals(req.getFollowedEmail().getEmail())
                    ))
                    .map(Relation::getFollowedEmail)
                    .map(RelationResponseDto::from)
                    .collect(Collectors.toList());

            if (mutualFriends.isEmpty()) {
                throw new CustomException(ErrorCode.FRIEND_LIST_EMPTY);
            }

            return mutualFriends;

        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.FRIEND_LIST_FETCH_FAILED);
        }

    }
    public void sendFriendRequest(String fromEmail, String toEmail) {

        if (fromEmail.equals(toEmail)) {
            throw new CustomException(ErrorCode.SELF_FRIEND_REQUEST);
        }

        User fromUser = userRepository.findByIdOrElseThrow(fromEmail);
        User toUser = userRepository.findByIdOrElseThrow(toEmail);

        if (relationRepository.existsByFollowingEmailAndFollowedEmail(fromUser, toUser)) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ALREADY_SENT);
        }

        try {
            Relation relation = new Relation();
            relation.setFollowingEmail(fromUser);
            relation.setFollowedEmail(toUser);
            relationRepository.save(relation);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_SAVE_FAILED);
        }
    }
}
