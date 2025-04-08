package com.example.newsfeed.board;

import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    @Override
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto) {

        Optional<User> userByNickname = userRepository.findUserByNickname(boardRequestDto.getNickname());

        if(userByNickname.isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Board board = new Board(boardRequestDto.getTitle(), boardRequestDto.getContents(), boardRequestDto.getImage());
        board.setUser(userByNickname.get());

        Board saveBoard = boardRepository.save(board);

        return new BoardResponseDto(saveBoard);
    }
}
