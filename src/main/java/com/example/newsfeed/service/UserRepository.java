package com.example.newsfeed.service;

import com.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;

public interface UserRepository extends JpaRepository<User,Long> {



}
