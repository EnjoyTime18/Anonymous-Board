package com.board.domain.dto;

import java.time.LocalDateTime;

import com.board.domain.entity.Board;

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
public class BoardDTO {
	
	private Long no; // 게시글의 Primary Key를 나타내는 필드입니다.
	
	private String writer; // 게시글을 작성한 사람의 이름을 저장할 필드입니다.
	
	private String title; // 게시글의 제목을 저장할 필드입니다.
	
	private String content;  // 게시글의 내용을 저장할 필드입니다.
	
	private String password; // 게시글의 비밀번호를 저장할 필드입니다.
	
	private int readcount; // 게시글의 조회수를 저장할 필드입니다.
	
	private LocalDateTime writeTime; // 게시글이 작성된 시간을 저장할 필드입니다.
	
	private LocalDateTime editTime; // 게시글이 마지막으로 수정된 시간을 저장할 필드입니다.
	
	private long replyCount; // 게시글에 달린 댓글의 개수를 저장할 필드입니다.
	
	// Board 엔터티 객체를 받아 해당 객체를 BoardDTO 객체로 변환하는 정적 메서드입니다.
	public static BoardDTO toDTO(Board board) {
        return BoardDTO.builder()
                .no(board.getNo())
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent())
                .password(board.getPassword())
                .readcount(board.getReadcount())
                .writeTime(board.getWriteTime())
                .editTime(board.getEditTime())
                .replyCount(board.getReplyCount())
                .build();
    }

}
