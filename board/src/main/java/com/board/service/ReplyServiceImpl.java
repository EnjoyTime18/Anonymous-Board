package com.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.domain.dto.BoardDTO;
import com.board.domain.dto.ReplyDTO;
import com.board.domain.entity.Board;
import com.board.domain.entity.Reply;
import com.board.repository.BoardRepository;
import com.board.repository.ReplyRepository;

@Service
public class ReplyServiceImpl implements ReplyService {

	private BoardRepository boardRepository;
	private ReplyRepository replyRepository;

	@Autowired
	public ReplyServiceImpl(BoardRepository boardRepository, ReplyRepository replyRepository) {
		this.boardRepository = boardRepository;
		this.replyRepository = replyRepository;
	}

	// 댓글을 저장하고, 저장된 댓글과 관련 게시글 정보를 반환
	@Override
	public ReplyDTO saveReply(BoardDTO boardDTO, ReplyDTO replyDTO) {
		// 클라이언트에서 넘어온 댓글 데이터를 바탕으로 댓글 엔터티를 생성 및 저장
		Reply reply = replyRepository.save(Reply.builder() // 클라이언트 댓글 데이터값 넘어온거 -> 댓글 엔티티 값 세팅 
												.writer(replyDTO.getWriter())
												.content(replyDTO.getContent())
												.password(replyDTO.getPassword())
												.board(Board.toEntity(boardDTO)).build()); 

		// 해당 게시글의 연관된 댓글 개수를 갱신합니다.
	    boardDTO.setReplyCount(replyRepository.countAllByBoard(Board.toEntity(boardDTO)));

	    // 해당(기존) 게시글의 정보를 가져옵니다.
	    Board existingBoard = boardRepository.findById(boardDTO.getNo()).orElseThrow();
	    // 해당 게시글의 댓글 개수를 업데이트합니다. 
	    existingBoard.setReplyCount(boardDTO.getReplyCount());
	    // 업데이트된 정보로 게시글을 저장합니다. 
	    boardRepository.save(existingBoard); // 갱신된 댓글 개수로 게시글 정보를 업데이트

	    // 저장된 댓글 엔터티를 다시 DTO로 변환하여 반환
	    ReplyDTO resultReplyDTO = ReplyDTO.toDTO(reply);
	    
	    // 저장된 댓글에 해당하는 게시글의 정보를 댓글 DTO에 함께 저장
	    // 이렇게 함께 저장하는 이유는 댓글 DTO가 어느 게시글에 속한 댓글인지의 정보를 함께 가지도록 하기 위함
	    // 클라이언트 측에서 댓글과 함께 해당 댓글의 게시글 정보가 필요할 경우 사용되는 패턴
	    // 나중에 특정 댓글을 조회할 때 해당 댓글이 어떤 게시글에 속하는지를 쉽게 알 수 있게 하기 위함.
	    resultReplyDTO.setBoardDTO(boardDTO);
	    
	    return resultReplyDTO;
	}

	// 특정 게시글의 모든 댓글을 최신 순으로 조회
	@Override
	public List<ReplyDTO> findAllByBoardNoDesc(BoardDTO boardDTO) {
		List<Reply> allReply = replyRepository.findAllByBoardOrderByNoDesc(Board.toEntity(boardDTO)); //최신순

		return allReply.stream().map(ReplyDTO::toDTO).collect(Collectors.toList());
	}

	// 특정 게시글의 모든 댓글을 오래된 순으로 조회
	@Override
	public List<ReplyDTO> findAllByBoardNoAsc(BoardDTO boardDTO) {
		List<Reply> allReply = replyRepository.findAllByBoardOrderByNoAsc(Board.toEntity(boardDTO)); //작성순

		return allReply.stream().map(ReplyDTO::toDTO).collect(Collectors.toList());
	}

	// 특정 게시글에 달린 댓글의 총 개수를 반환
	@Override
	public long countAllBoard(BoardDTO boardDTO) {
		return replyRepository.countAllByBoard(Board.toEntity(boardDTO));
	}
	
	// 댓글 번호를 기준으로 특정 댓글의 상세 정보를 조회
	@Override
	public ReplyDTO detailReply(long no) {
		Reply reply = replyRepository.findById(no).orElseThrow();
				
		return ReplyDTO.toDTO(reply);
	}
	
	// 특정 댓글을 수정하는 메서드
	@Override
	public ReplyDTO editBoard(long no, ReplyDTO editreplyDTO) {
		// 수정할 댓글의 원본을 가져옵니다.
		Reply origin = replyRepository.findById(no).orElseThrow();
				
		// 댓글 정보를 수정합니다.
		origin.setWriter(editreplyDTO.getWriter());
		origin.setContent(editreplyDTO.getContent());
		origin.setPassword(editreplyDTO.getPassword());
		
		// 수정된 정보를 저장합니다.
		Reply updateReply = replyRepository.save(origin);
		
		// 수정된 댓글을 DTO로 변환하여 반환
		return ReplyDTO.toDTO(updateReply);				
	}
	
	// 특정 댓글을 삭제하고, 해당 게시글의 댓글 개수를 갱신하는 메서드
	@Override
	public void deleteReply(long replyId) {
		// 삭제할 댓글을 가져옵니다.
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        
        // 조회된 해당 댓글을 삭제합니다.
        replyRepository.deleteById(replyId);

        // 삭제된 댓글이 속한 게시글을 가져옵니다.
        Board board = reply.getBoard();
        
        // 게시글의 댓글 개수(replyCount 값)를 감소시킵니다.
        board.setReplyCount(board.getReplyCount() - 1);
        boardRepository.save(board); // 변경된 게시글의 댓글 개수 정보를 데이터베이스에 업데이트합니다.
	}

}
