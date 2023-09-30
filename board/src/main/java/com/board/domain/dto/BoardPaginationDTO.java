package com.board.domain.dto;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.ToString;

@Getter 
@ToString
//게시판의 페이지 네비게이션 정보 관리와 페이징 기능을 위한 DTO 클래스
public class BoardPaginationDTO {
	private int current; //현재 페이지 번호
	private int begin; //시작 페이지 번호
	private int end; //종료 페이지 번호
	private int total; //전체 페이지 개수
	private int size = 10; // 한 화면에 보여줄 페이지 번호 개수 (예: 1, 2, 3, ... 10 | 1~10, 11~20, ...)
	
	private final int blockSize = 10; // 페이지의 블록 크기 (예: 1-10, 11-20, ...). 한 블록에 페이지 번호 10개씩 표시
									  // 페이지 블록의 크기. 예를 들어, 10으로 설정하면 1~10, 11~20, 21~30 등과 같이 10개의 페이지로 구분
	 								  // 페이지를 한 번에 몇 개씩 그룹화해서 보여줄 것인지, 한 블록에 포함되는 페이지 수. 예: 블록1(1-10), 블록2(11-20)

	
	// 생성자를 통해 Page 객체에서 페이지네이션에 필요한 정보를 추출 (페이징 정보 초기화)
	public BoardPaginationDTO(Page<BoardDTO> page) {
		this.total = page.getTotalPages(); // 전체 페이지 수 설정, 전체 페이지 수를 가져옴
		this.current = page.getNumber() + 1; // 현재 페이지 번호 설정. Spring Data JPA는 페이지 번호가 0부터 시작하므로 +1 해준다.
		this.begin = (current-1) / size * size + 1; // 시작 페이지 번호 계산
		this.end = Math.min(this.begin + size - 1, this.total); // 종료 페이지 번호 계산. 전체 페이지 수를 넘어가지 않도록 Math.min 함수를 사용하여 보정함.
											                    // 마지막 페이지가 전체 페이지 수를 초과하지 않게 계산
	}
	
	//처음(first), 이전(previous), 다음(next), 마지막(last)에 대한 정보
	//현 페이지 블록의 처음/이전/다음/마지막 페이지 유무 확인 메서드들
	public boolean hasFirstBlock() { // 현재 페이지가 첫 페이지 이후인지 확인 (첫 페이지로 이동할 수 있는지)
		return current > 1;  // 현재 페이지 번호가 1보다 크면 첫 페이지로 갈 수 있는 버튼이 활성화되어야 함.
	}
	public boolean hasPreviousBlock() { // 이전 페이지 블록이 있는지 확인 (예: 현재 페이지가 15면 11-20 페이지 블록의 이전인 1-10 페이지 블록으로 이동할 수 있는지)
	    return (current - 1) / blockSize > 0; // 현재 페이지가 첫 번째 블록에 속하지 않는다면, 즉 현재 페이지 번호가 11 이상이면, 이전 블록으로 갈 수 있는 버튼이 활성화되어야 함.
	}
	public boolean hasNextBlock() { // 다음 페이지 블록이 있는지 확인
		return end < total; // 현재 블록의 마지막 페이지가 전체 페이지 수보다 작다면, 다음 블록으로 이동할 수 있어야 함.
	}
	public boolean hasLastBlock() { // 현재 페이지가 마지막 페이지 전인지 확인 (마지막 페이지로 이동할 수 있는지)
		return current < total; // 현재 페이지 번호가 전체 페이지 수보다 작다면, 마지막 페이지로 이동할 수 있어야 함.
	}
	
	// JPA는 페이지 번호가 0부터 시작하므로, 각 페이지 관련 메서드에선 -1 처리를 해줌
	public int getFirst() { // 첫 번째 페이지 번호 반환
		return 1 -1;
	}
	public int getLast() { // 마지막 페이지 번호 반환
		return total -1;
	}
	public int getPrevious() { // 이전 페이지 블록의 첫 페이지 번호 반환, 이전 블록의 첫 페이지를 반환
		// 현재 페이지 블록의 첫 페이지 번호를 구한다.
	    int prevBlockFirstPage = ((current - 1) / blockSize) * blockSize; 
	    // 이전 페이지 블록의 마지막 페이지 번호를 반환
	    return prevBlockFirstPage - blockSize + 1 - 1;
	}
	public int getNext() { // 다음 페이지 블록의 첫 페이지 번호를 반환, 다음 블록의 첫 페이지를 반환 
		return end + 1 -1;
	}
	
}