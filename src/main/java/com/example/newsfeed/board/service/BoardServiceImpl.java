package com.example.newsfeed.board.service;

import com.example.newsfeed.board.dto.BoardRequestDto;
import com.example.newsfeed.board.dto.BoardResponseDto;
import com.example.newsfeed.board.repository.BoardRepository;
import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.Relation;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.friends.repository.RelationRepository;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    private final RelationRepository relationRepository;

    @Override
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto) {

        Optional<User> userByEmail = userRepository.findUserByEmail(boardRequestDto.getEmail());

        if(userByEmail.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Board board = new Board(boardRequestDto.getTitle(), boardRequestDto.getContents(), boardRequestDto.getImage());
        board.setUser(userByEmail.get());

        Board saveBoard = boardRepository.save(board);

        return new BoardResponseDto(saveBoard);
    }

    @Override
    @Transactional
    public List<BoardResponseDto> findAllBoardsByMeAndFriends(String email) {

        List<User> friendList = getFriendList(email);

        User me = userRepository.findByIdOrElseThrow(email);

        friendList.add(me);

        List<Board> findBoardList = boardRepository.findByUserInOrderByModifiedAtDesc(friendList);

        return findBoardList.stream().map(BoardResponseDto::new).toList();
    }

    @Override
    public BoardResponseDto findBoardById(Long id, String email) {

        List<User> friendList = getFriendList(email);

        Board findBoardById = boardRepository.findBoardByIdOrElseThrow(id);

        if(!friendList.contains(findBoardById.getUser())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return new BoardResponseDto(findBoardById) ;
    }

    private List<User> getFriendList(String email){
        List<Relation> sentRequests = relationRepository.findByFollowingEmail_Email(email);
        List<Relation> receivedRequests = relationRepository.findByFollowedEmail_Email(email);

        return new ArrayList<>(sentRequests.stream()
                .map(Relation::getFollowedEmail)
                .filter(followedEmail -> receivedRequests.stream().anyMatch(
                        r -> r.getFollowingEmail().getEmail().equals(followedEmail.getEmail())
                ))
                .toList());
    }


    @Transactional
    @Override
    public BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, Long id, String email) {

        Board findBoardById = boardRepository.findBoardByIdOrElseThrow(id);

        if(!findBoardById.getUser().getEmail().equals(email)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Modifications are permitted only by the original author.");
        }

        if(boardRequestDto.getTitle() != null){
            findBoardById.setTitle(boardRequestDto.getTitle());
        }

        if(boardRequestDto.getContents() != null){
            findBoardById.setContents(boardRequestDto.getContents());
        }

        if(boardRequestDto.getImage() != null){
            findBoardById.setImage(boardRequestDto.getImage());
        }

        return new BoardResponseDto(findBoardById);
    }

    @Override
    public void deleteBoard(Long id, String email) {

        Board findBoardById = boardRepository.findBoardByIdOrElseThrow(id);

        if(!findBoardById.getUser().getEmail().equals(email)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Deletion is permitted only for the original author.");
        }

        boardRepository.delete(findBoardById);

    }


}
