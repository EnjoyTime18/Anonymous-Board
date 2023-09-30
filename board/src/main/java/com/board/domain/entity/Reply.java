package com.board.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.board.domain.dto.ReplyDTO;

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

@Entity
@Table(name="REPLY")
public class Reply {
	
	//댓글 pk
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long no;
	
	//댓글 작성자
	@Column
	private String writer; // 댓글을 작성한 사람의 이름을 저장할 필드입니다.
	
	//댓글 내용
	@Column 
	@Lob
	private String content; // 댓글의 내용을 저장할 필드입니다. 큰 텍스트를 저장하기 위해 @Lob 어노테이션을 사용합니다.
	
	@Column(length = 20)
	private String password; // 댓글의 비밀번호를 저장할 필드입니다. 최대 길이는 20입니다.
	
	//댓글 작성 시간
	@CreationTimestamp
	private LocalDateTime writeTime;  // 댓글이 작성된 시간을 저장할 필드입니다. 자동으로 현재 시간이 저장됩니다.
	
	// 게시글과 n:1관계 설정 및 삭제 시 자동 소멸 처리
	// Board 엔터티와의 관계를 나타냅니다. 여러 댓글은 하나의 게시글에 속할 수 있습니다(n:1 관계).
	// 외래키 컬렴명을 board_no로 설정
//	@ManyToOne(targetEntity = Board.class, cascade = CascadeType.REMOVE) // Reply 엔터티에 CascadeType.REMOVE가 적용되어 있기 때문에 Reply를 삭제할 때 연관된 Board가 삭제되려고 하는 문제점 발생
	@ManyToOne(targetEntity = Board.class)
	@JoinColumn(name = "board_no")
	private Board board;
	
	// DTO 객체를 받아 해당 객체를 Reply 엔터티 객체로 변환하는 정적 메서드입니다.
	public static Reply toEntity(ReplyDTO replyDTO) {
		return Reply.builder()
				.no(replyDTO.getNo())
				.writer(replyDTO.getWriter())
				.content(replyDTO.getContent())
				.password(replyDTO.getPassword())
				.writeTime(replyDTO.getWriteTime())
				.board(Board.toEntity(replyDTO.getBoardDTO()))
				.build();
	}
	
}
