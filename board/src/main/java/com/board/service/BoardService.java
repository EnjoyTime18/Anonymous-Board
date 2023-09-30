package com.board.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.board.domain.dto.BoardDTO;
import com.board.domain.dto.BoardSearchDTO;

public interface BoardService {	
	BoardDTO saveBoard(BoardDTO boardDTO);
	
	Page<BoardDTO> findAllBoard(BoardSearchDTO boardSearchDTO, Pageable pageable);
	
	BoardDTO detailBoard(long no, boolean increaseViewCount); //boolean increaseViewCount 조회수 증가 결정 여부 (증가O : true, 증가X : false )
	
	BoardDTO editBoard(long no, BoardDTO boardDTO);
	
	void deleteBoard(long no);
}
