package com.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "relation")
@Getter
@NoArgsConstructor
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "following_email")
    private User followingEmail;

    @Setter
    @ManyToOne
    @JoinColumn(name = "followed_email")
    private User followedEmail;


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

}
