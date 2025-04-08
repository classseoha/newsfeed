package com.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "longtext")
    private String contents;

    private String image;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Board(String title, String contents, String image) {
        this.title = title;
        this.contents = contents;
        this.image = image;
    }
}
