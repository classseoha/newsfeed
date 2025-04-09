package com.example.newsfeed.friends.repository;

import com.example.newsfeed.entity.Relation;
import com.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
    List<Relation> findByFollowingEmail_Email(String followingEmail);
    List<Relation> findByFollowedEmail_Email(String followedEmail);
    boolean existsByFollowingEmailAndFollowedEmail(User followingEmail, User followedEmail);
}
