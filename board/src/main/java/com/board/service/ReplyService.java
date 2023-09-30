package com.board.service;

import java.util.List;

import com.board.domain.dto.BoardDTO;
import com.board.domain.dto.ReplyDTO;

public interface ReplyService {	
	ReplyDTO saveReply(BoardDTO boardDTO, ReplyDTO replyDTO);
	
	List<ReplyDTO> findAllByBoardNoDesc(BoardDTO boardDTO);	
	List<ReplyDTO> findAllByBoardNoAsc(BoardDTO boardDTO);
	
	long countAllBoard(BoardDTO boardDTO);
	
	ReplyDTO detailReply(long no);
	
	ReplyDTO editBoard(long no, ReplyDTO editreplyDTO);
	
	void deleteReply(long no);
}