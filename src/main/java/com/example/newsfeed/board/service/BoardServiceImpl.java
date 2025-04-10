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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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

        if(boardRequestDto.getContents() == null || boardRequestDto.getTitle() == null){
            throw new IllegalArgumentException("입력값이 올바르지 않습니다.");
        }

        Optional<User> userByEmail = userRepository.findUserByEmail(boardRequestDto.getEmail());

        if(userByEmail.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "사용자를 찾을 수 없습니다.");
        }

        Board board = new Board(boardRequestDto.getTitle(), boardRequestDto.getContents(), boardRequestDto.getImage());
        board.setUser(userByEmail.get());

        Board saveBoard = boardRepository.save(board);

        return new BoardResponseDto(saveBoard);
    }

    @Override
    @Transactional
    public Page<BoardResponseDto> findAllBoardsByMeAndFriends(String email, Integer page, Integer size) {

        int adjustedPage = (page > 0) ? page - 1 : 0;

        if(size < 0){
            throw new IllegalArgumentException("잘못된 페이지 번호입니다.");
        }

        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("modifiedAt").descending());

        List<User> friendList = getFriendList(email);

        User me = userRepository.findByIdOrElseThrow(email);

        friendList.add(me);

        Page<Board> boards = boardRepository.findByUserIn(friendList, pageable);

        return boards.map(BoardResponseDto::new);
    }

    @Override
    public BoardResponseDto findBoardById(Long id, String email) {

        List<User> friendList = getFriendList(email);

        User me = userRepository.findByIdOrElseThrow(email);

        friendList.add(me);

        Board findBoardById = boardRepository.findBoardByIdOrElseThrow(id);

        if(!friendList.contains(findBoardById.getUser())){
            throw new AccessDeniedException("작성자의 친구가 아니라 접근이 불가능합니다.");
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
            throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
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
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }

        boardRepository.delete(findBoardById);

    }


}
