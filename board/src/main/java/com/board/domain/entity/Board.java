package com.board.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.board.domain.dto.BoardDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor

@Builder // 빌더 패턴을 사용하기 위한 어노테이션입니다. 객체 생성 시 유동적인 패턴을 사용할 수 있게 해줍니다.

@Setter
@Getter
@ToString

@Entity
@Table(name = "BOARD")
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long no; // Primary Key임을 지정하며, 자동 생성되는 값을 사용합니다.
	
	@Column(length = 60)
	private String writer; // 글 작성자의 정보를 저장할 필드. 최대 길이는 60입니다.
	
	@Column(length = 300) 
	private String title; // 글 제목을 저장할 필드. 최대 길이는 300입니다.
	
	@Column 
	@Lob
	private String content; // 글의 본문 내용을 저장할 필드. 큰 텍스트를 저장하기 위해 @Lob 어노테이션을 사용합니다.
	
	@Column(length = 20)
	private String password; // 글의 비밀번호를 저장할 필드. 최대 길이는 20입니다.
	
	@Column
	private int readcount; // 글의 조회수를 저장할 필드입니다.
	
	@CreationTimestamp
	private LocalDateTime writeTime; // 글이 작성된 시간을 저장할 필드. 자동으로 현재 시간이 저장됩니다.
	
	@UpdateTimestamp
	private LocalDateTime editTime; // 글이 마지막으로 수정된 시간을 저장할 필드. 자동으로 현재 시간이 갱신됩니다.
	
	//댓글 개수 확인용 컬럼
	//- 조인을 해도 셀 수 있지만 성능상의 이점을 가지기 위해 별도의 컬럼을 설정
	@Column(name = "reply_count")
	private long replyCount; // 해당 게시글에 대한 댓글 개수를 저장할 필드입니다.
		
	// Board 엔터티와 Reply 엔터티의 관계를 설정합니다. 하나의 게시글에는 여러 개의 댓글이 있을 수 있습니다.
    // Board 엔터티가 삭제될 때 연결된 모든 Reply 엔터티도 함께 삭제됩니다.
	@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE) // Board가 삭제될 때 연관된 Reply가 삭제되는 것 -> Board 엔터티가 삭제될 때 연관된 모든 Reply 엔터티도 삭제
	private List<Reply> replies;
	
	// DTO 객체를 받아 해당 객체를 Board 엔터티 객체로 변환하는 정적 메서드입니다.
	public static Board toEntity(BoardDTO boardDTO) { // 엔티티 객체에 빌더패턴이 정의되어 었으면 구지 ModelMapper 객체 사용하지 않아도됨. dto -> entity 변환
        return Board.builder()
                .no(boardDTO.getNo())
                .writer(boardDTO.getWriter())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .password(boardDTO.getPassword())
                .readcount(boardDTO.getReadcount())          
                .replyCount(boardDTO.getReplyCount())
                .build();
    }

}
