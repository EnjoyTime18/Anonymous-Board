package com.board.domain.dto;

import java.time.LocalDateTime;

import com.board.domain.entity.Reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder

@Setter
@Getter
@ToString
public class ReplyDTO {
	
	private Long no; // 댓글의 Primary Key를 나타내는 필드입니다.
	
	private String writer; // 댓글을 작성한 사람의 이름을 저장할 필드입니다.
	
	private String content; // 댓글의 내용을 저장할 필드입니다.
	
	private String password; // 댓글의 비밀번호를 저장할 필드입니다.
	
	private LocalDateTime writeTime; // 댓글이 작성된 시간을 저장할 필드입니다.
	
	private BoardDTO boardDTO; // 해당 댓글이 어떤 게시글에 속하는지를 나타내는 BoardDTO 객체를 저장할 필드입니다.
	
	// Reply 엔터티 객체를 받아 해당 객체를 ReplyDTO 객체로 변환하는 정적 메서드입니다.
	public static ReplyDTO toDTO(Reply reply) {
		return ReplyDTO.builder()
				.no(reply.getNo())
				.writer(reply.getWriter())
				.content(reply.getContent())
				.password(reply.getPassword())
				.writeTime(reply.getWriteTime())
				// Reply 엔터티의 Board 객체를 BoardDTO 객체로 변환합니다.
				.boardDTO(BoardDTO.toDTO(reply.getBoard()))
				.build();
	}

}
