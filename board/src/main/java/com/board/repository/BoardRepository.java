package com.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.board.domain.entity.Board;

//JpaSpecificationExecutor는 복잡한 쿼리 작업을 위한 인터페이스로, 조건별 검색 등을 수행할 수 있습니다.
//JpaSpecificationExecutor를 상속받아 복잡한 쿼리를 동적으로 생성하고 실행할 수 있게 합니다.
//복잡한 조건의 쿼리를 실행하기 위한 JPA 인터페이스
@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, JpaSpecificationExecutor<Board> {

	// 목록 최신순(번호 내림차순)
	List<Board> findAllByOrderByNoDesc();
}
