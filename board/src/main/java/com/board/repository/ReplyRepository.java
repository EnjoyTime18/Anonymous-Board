package com.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.board.domain.entity.Board;
import com.board.domain.entity.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

	// 게시글에 따라 처리해야 하므로 다음 기능을 구현
	// - 특정 게시글의 댓글 목록
//	List<Reply> findAllByBoard(Board board); //사용 안함

	List<Reply> findAllByBoardOrderByNoDesc(Board board); // 주어진 게시글(Board)에 연관된 모든 댓글(Reply)을 'no' 필드(번호)의 내림차순으로 검색합니다. 즉, 가장 최근의 댓글부터 반환됩니다.

	List<Reply> findAllByBoardOrderByNoAsc(Board board); // 주어진 게시글(Board)에 연관된 모든 댓글(Reply)을 'no' 필드(번호)의 오름차순으로 검색합니다. 즉, 가장 오래된 댓글부터 반환됩니다.

	// - 특정 게시글의 댓글 개수
	// 주어진 게시글(Board)에 연관된 댓글(Reply)의 개수를 반환합니다.
	// 이는 예를 들어, 게시글 별 댓글의 수를 화면에 표시할 때 사용될 수 있습니다.
	// 이 메서드는 findAllByBoard(Board board)와 비슷한 역할을 하지만, 결과로 댓글의 개수만 반환합니다.
	long countAllByBoard(Board board); // findAllByBoard(Board board) 대체
	
	// 특정 게시글에 달린 댓글의 총 개수를 반환하는 메서드입니다.
	// 이 메서드는 위의 findAllByBoard 메서드와 유사한 기능을 수행하지만,
	// 반환값이 댓글의 목록이 아니라 댓글의 개수입니다.
}
