package com.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}
	
//	2023-09-19 저녁 9시 기준 문제점
//	문제점 : 검색 한 후 초기화 버튼 클릭 시 작동 안하는 문제 즉, 상자 안에 검색한 조건값 세팅 초기화 안됨.
//	       초기화 목록 검색 버튼 간격 띄우기 마진값 세팅
//		   + 헤더부분 지저분한거 지우기 -> 헤더 검색창 제거, 테스트 꾸미미
	
//이 코드를 면접에서 설명을 해달라고 질문이 들어올수도 있어서 그러는데 면접관분들 한테 이 코드를 알기 쉽게 코드가 돌아가는(실행되는) 흐름, 동작, 핵심 설명을 해줄수 있게 전체 코드들을 한줄 한줄씩 주석을 달아줄래?
	
	/* 기능
	 * 1. 게시글 crud -> 조회, 상세보기 기능 | 게시글 등록 , 수정, 삭제 기능
	 * 2. 댓글 crud -> 댓글 등록, 조회 기능-> 비동기 | 댓글 수정, 삭제 기능 -> 동기)
	 * 3. 게시글 복합 검색 처리
	 * 4. 게시글 페이징 처리
	 * 
	 * 댓글 사진 : 
	 * 
	 * 
	 */
}


/*
 *  6차사용 시

애프터이미지 쇼크 - 윌 오브 리버티 - 메여축 - 벙버 - 맥시마이즈 캐논 - 엔버링크 - 시드링 - 오리진 - 버닝브레이커 - 바인드 - 발칸펀치


2) 6차사용 x

애프터이미지 쇼크 - 윌 오브 리버티 - 메여축 - 엔버링크 - 바인드 - 시드링 - 발칸펀치 - 맥시마이즈 캐논 - 버닝브레이커 - 벙버

[또뀨] [오전 1:32] 이렇게 쓰는게 제일 무난한것 같긴해요
[또뀨] [오전 1:32] 오리진 때문에 극딜주기 10초 밀리니까
[또뀨] [오전 1:32] 버브 발칸 먼저 쓰고 오리진 나중에 쓰시는분들도 있긴하더라고요


오리진 - 버브 - 바인드 - 발칸 순으로쓰면
바인드 거의 20초 풀로 가능
 */