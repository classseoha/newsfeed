package com.example.newsfeed.user.repository;

import com.example.newsfeed.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findByEmail(String email);


    Optional<User> findUserByEmail(String email);


    default User findByIdOrElseThrow(String email){
        return findByEmail(email).orElseThrow(()->
                new EntityNotFoundException("해당 이메일의 유저가 존재하지 않습니다."));
    }

    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    boolean existsByNicknameAndEmailNot(String nickname, String email);
}