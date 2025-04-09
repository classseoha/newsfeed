package com.example.newsfeed.friends.repository;

import com.example.newsfeed.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
    List<Relation> findByFollowingEmail_Email(String email);
    List<Relation> findByFollowedEmail_Email(String email);
}
