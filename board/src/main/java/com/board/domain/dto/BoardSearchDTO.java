package com.board.domain.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.board.domain.entity.Board;
import com.board.util.EmptyChecker;

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
//검색 조건을 담기 위한 DTO. 동적 쿼리 생성을 위해 JpaSpecificationExecutor 사용
public class BoardSearchDTO {
	// 검색 조건에 사용될 변수들
	private Long no; // 게시글 번호
	private String writer, title, content, begin, end;  // 작성자, 제목, 내용, 검색 시작일, 검색 종료일
	
	// 검색 필드 중 어떤 필드가 설정되어 있는지 체크하여 검색을 실행할지 결정하는 메서드, 검색 여부 판정 메소드 정의 
	// 검색 조건이 하나라도 설정되어 있는지 판단, 검색 조건이 설정되었는지 확인
	// 어떠한 검색 조건이 설정되었는지 확인
	public boolean isSearch() {
//		return (no != null && no > 0L) || (writer != null && !writer.isEmpty()) ||
//				(title != null && !title.isEmpty()) || (content != null && !content.isEmpty()) ||
//						(begin != null && !begin.isEmpty()) || (end != null && end.isEmpty());		
		// 기존 검색 로직은 각 필드를 하나씩 검사
        // 하지만 EmptyChecker 유틸리티를 사용하여 코드를 간결하게 표현
		
		// EmptyChecker 클래스를 활용하여 입력값들이 비어있는지 체크. 즉, 각각의 검색 조건이 비어있지 않은지 확인
		// 필드 값 중 하나라도 비어있지 않다면 검색 조건이 있다고 판단
		// 검색 조건들 중 하나라도 빈 값이 아니면 true 반환
		return !EmptyChecker.isEmpty(no, writer, title, content, begin, end);
	}
	

	// 검색 조건에서 문자열 형식의 검색 시작 날짜를 LocalDateTime 객체로 반환
	public LocalDateTime beginLocalDateTime() {
	    return LocalDateTime.parse(begin + "T00:00:00"); // yyyy-MM-dd 형식의 문자열을 LocalDateTime으로 변환
	}
	
	// 검색 조건에서 문자열 형식의 검색 종료 날짜를 LocalDateTime 객체로 반환
	public LocalDateTime endLocalDateTime() {
	    return LocalDateTime.parse(end + "T23:59:59"); // yyyy-MM-dd 형식의 문자열을 LocalDateTime으로 변환
	}
	
	// JPA Specification을 위한 spec 생성
	// JPA Specification을 활용한 동적 쿼리 생성 메서드, 검색 조건에 맞는 쿼리를 생성
	// 검색 조건에 따라 쿼리를 생성. 이를 통해 동적 쿼리 생성이 가능
	public Specification<Board> specification(){
		// 실제 쿼리 조건을 생성하기 위한 Specification 인터페이스의 toPredicate 메서드를 구현
		return (root, query, builder) -> {
			
			/* Predicate는 WHERE 절에 들어갈 조건들을 표현하는 객체
			 * Specification을 사용해서 동적 쿼리를 생성할 때, 여러 가지 조건들을 동적으로 생성하고 조합하기 위해 Predicate 객체들의 리스트를 사용
			 * 예를 들어, 사용자가 게시판의 검색 폼에서 작성자와 제목을 입력했다고 가정하면, 두 조건 모두 WHERE 절에 들어가야 함. 
			 * 이런 경우, Predicate 리스트에 두 조건을 추가하고, 이를 조합하여 하나의 Predicate로 만들어 쿼리에 적용할 수 있음.
			 * 
			 * WHERE name = 'John' 이라는 조건이 있다면, 이는 JPA Criteria API에서 Predicate로 표현 됨.
			 * 
			 * 실제 SQL에서 WHERE 조건에 해당하는 부분을 JPA에서는 Predicate 객체로 표현하는 것입니다.
			 * 
			 */
			//List<Predicate>는 여러 개의 다양한 조건을 담아두기 위한 것, 검색 조건에 따라 여러 조건을 동적으로 추가하고, 이후에 이들 조건을 결합하여 최종 쿼리를 생성하는 데 사용
			//필요한 조건을 리스트에 추가하고, 모든 조건을 결합하여 하나의 쿼리를 생성
			List<Predicate> predicates = new ArrayList<>(); // 검색 조건들을 담기 위한 리스트
			
			// 각 필드의 값이 설정되어 있을 때 해당 필드로 검색 조건을 추가
			
			// 게시글 번호가 설정되어 있다면 조건 추가, 게시글 번호가 검색 조건으로 주어진 경우
			if(!EmptyChecker.isEmpty(this.no)) {
				predicates.add(builder.equal(root.get("no"), this.no));
				
				/* 실행 흐름(공부) 설명
				 * 1-1
				 *  Board 엔터티의 no 필드에 접근
				 *  no 필드의 값과 this.no의 값을 비교하는 Predicate(DB SQL WHERE절)객체를 생성
				 *  이 Predicate 객체를 predicates 리스트에 추가 
				 *  이 코드는 DB에서 조회할 때, Board 엔터티의 no 필드 값이 this.no와 동일한 데이터만을 가져오도록 하는 조건을 생성
				 * 
				 * 1-2
				 *  predicates.add(builder.equal(root.get("no"), this.no)); 동작 방식
				 *	이 코드를 세 부분으로 나누어 설명하겠습니다:
				 *	
				 *	root.get("no"): 여기서 root는 쿼리의 대상 엔티티를 참조합니다. root.get("no")는 엔티티의 no 속성을 참조합니다. SQL로 치면 테이블의 no 컬럼을 의미합니다.
				 *	
				 *	builder.equal(...): CriteriaBuilder 인스턴스인 builder를 사용하여 Predicate 객체를 생성합니다. equal 메서드는 두 인자 값이 동일한지를 확인하는 조건을 만듭니다. 
				 *	즉, SQL의 = 연산자와 동일한 역할을 합니다.
				 *	
				 *	predicates.add(...): 생성된 Predicate 객체 (여기서는 no 속성 값이 this.no 값과 같은지를 확인하는 조건)를 predicates 리스트에 추가합니다.	
				 *	따라서, 이 코드의 전체 동작은 검색 조건으로 주어진 no 값이 있을 경우, 해당 no 값을 가진 엔티티를 찾기 위한 조건을 predicates 리스트에 추가하는 것입니다.
                 *
                 * 1-3
				 *	root.get("no"):
				 *	root는 쿼리의 대상 엔티티(Board 엔티티)를 의미합니다.
				 *	get("no")는 Board 엔티티의 no 필드에 접근하는 메서드입니다. 이는 SQL 쿼리의 Board.no에 해당합니다.
				 *
				 *	builder.equal(root.get("no"), this.no):
				 *	builder는 CriteriaBuilder 객체로, 여러 쿼리 조건들을 생성하는 데 사용됩니다.
				 *	equal 메서드는 두 인자 값이 동일한지 확인하는 조건을 생성합니다.
				 *
				 *	root.get("no")는 Board 엔티티의 no 필드의 값을 의미, this.no는 현재 DTO의 no 값을 의미합니다.
				 *	따라서, 이 코드는 Board 엔티티의 no 필드 값이 현재 DTO의 no 값과 동일한지 확인하는 조건을 생성합니다. SQL로 표현하면 WHERE Board.no = ?와 같습니다.
				 *
				 *	predicates.add(...):
				 *	생성된 조건(Predicate)을 predicates 리스트에 추가합니다.
				 *	결과적으로, 이 코드는 사용자가 입력한 no 값이 있을 경우, 해당 번호에 해당하는 게시글만을 검색하기 위한 조건을 생성하고 리스트에 추가하는 역할을 합니다.
				 *
				 *  List에 저장된 각각의 Predicate 객체는 SQL 쿼리의 WHERE 절의 특정 조건을 표현하게 됩니다. 각각의 존재하는 검색 조건이 Predicate 객체로 되어 리스트에 저장되는 형태
				 *  
				 *  
				 *  root: 쿼리의 대상 엔터티에 대한 경로를 나타냅니다. 이를 통해 엔터티의 특정 속성이나 필드에 접근할 수 있습니다.
				 *	query: 현재 실행 중인 조회 쿼리를 나타냅니다. 이 객체를 통해 쿼리 자체를 수정하거나, 정렬 방식을 변경하거나, DISTINCT 등의 연산을 수행할 수 있습니다.
				 *  	   query 객체는 주로 복잡한 쿼리를 작성할 때 유용하게 사용됩니다. 예를 들면, 그룹화(GROUP BY), 집합 함수(예: SUM(), COUNT())를 사용하거나, 서브쿼리를 사용해야 할 때 등에 활용됩니다. 
				 *  	   지금의 예제에서는 기본적인 WHERE 절의 조건만 필요하기 때문에 query 객체를 직접 사용할 필요가 없었던 것입니다.  
				 *	builder: CriteriaBuilder 객체로, 쿼리의 조건 및 구조를 만들기 위한 여러 메서드들을 제공합니다. 이를 사용하여 Predicate 객체를 생성하고, 여러 조건을 조합합니다. 
				 */								
			}
			
			// 작성자가 설정되어 있다면 조건 추가, 작성자가 검색 조건으로 주어진 경우
			if(!EmptyChecker.isEmpty(this.writer)) {
				predicates.add(builder.equal(root.get("writer"), this.writer));
			}
			
			// 제목이 설정되어 있다면 조건 추가, 제목이 검색 조건으로 주어진 경우 (텍스트 내 검색)
			if(!EmptyChecker.isEmpty(this.title)) {
				//predicates.add(builder.equal(root.get("title"), this.title)); //일치 검색
				predicates.add(builder.like(root.get("title"), "%"+this.title+"%")); //유사(포함) 검색
			}
			
			// 내용이 설정되어 있다면 조건 추가, 내용이 검색 조건으로 주어진 경우 (텍스트 내 검색)
			if(!EmptyChecker.isEmpty(this.content)) {
				predicates.add(builder.like(root.get("content"), "%"+this.content+"%")); //유사(포함) 검색
			}
			
			// 검색 시작일과 종료일이 모두 설정되어 있다면 조건 추가, 기간(시작~종료 날짜)이 검색 조건으로 주어진 경우
			if(!EmptyChecker.isEmpty(this.begin) && !EmptyChecker.isEmpty(this.end)) {
			    predicates.add(builder.between(root.get("writeTime"), this.beginLocalDateTime(), this.endLocalDateTime()));
			}
			
			// 모든 검색 조건을 AND 연산으로 결합(조합)하여 반환
			return builder.and(predicates.stream().toArray(Predicate[]::new));
			// JPA가 실제로 실행할 때 이 리스트를 기반으로 아래와 같은 동적으로 SQL 쿼리의 WHERE 절을 생성하게 됨.
			// 존재하는 검색 조건이 게시글 번호, 작성자로 2가지 설정 되어 있는 경우 ex) : SELECT ... FROM Board WHERE no = ? AND writer = ?
		};
	}
	
}
