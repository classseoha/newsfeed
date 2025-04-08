package com.example.newsfeed.board;

import com.example.newsfeed.UserRepository;
import com.example.newsfeed.UserResponseDto;
import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

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
    public BoardResponseDto findBoardById(Long id) {

        Board findBoardById = boardRepository.findBoardByIdOrElseThrow(id);

        return new BoardResponseDto(findBoardById) ;
    }

    @Override
    public UserResponseDto findBoardCreatorById(Long id) {

        Board findBoardById = boardRepository.findBoardByIdOrElseThrow(id);

        return new UserResponseDto(findBoardById.getUser());
    }

    @Transactional
    @Override
    public BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, Long id) {

        Board findBoardById = boardRepository.findBoardByIdOrElseThrow(id);

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
    public void deleteBoard(Long id) {

        Board board = boardRepository.findBoardByIdOrElseThrow(id);

        boardRepository.delete(board);

    }


}
