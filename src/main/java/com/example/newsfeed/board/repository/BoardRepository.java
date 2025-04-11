package com.example.newsfeed.board.repository;

import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    default Board findBoardByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
    }

    Page<Board> findByUserIn(List<User> userList, Pageable pageable);
}
