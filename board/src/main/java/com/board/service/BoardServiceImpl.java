package com.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.board.domain.dto.BoardDTO;
import com.board.domain.dto.BoardSearchDTO;
import com.board.domain.entity.Board;
import com.board.repository.BoardRepository;

@Service
public class BoardServiceImpl implements BoardService {

	private BoardRepository boardRepository;

	@Autowired
	public BoardServiceImpl(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	@Override
	public BoardDTO saveBoard(BoardDTO boardDTO) {
		Board board = Board.toEntity(boardDTO);
		boardRepository.save(board);
		return BoardDTO.toDTO(board); // 저장된 Entity 객체를 다시 DTO 객체로 변환하여 반환
	}

	// 모든 게시글을 검색 조건에 따라 페이징하여 조회하는 메서드
	// 모든 게시글을 페이지네이션과 검색 조건을 적용하여 조회하는 메서드
	@Override
	public Page<BoardDTO> findAllBoard(BoardSearchDTO boardSearchDTO, Pageable pageable) {
//		List<Board> allBoard = boardRepository.findAll();
		
		// boardSearchDTO의 검색 조건과 pageable의 페이징 조건으로 게시글을 검색
		// 검색 조건과 페이징 정보를 적용하여 게시글을 조회
		// 동적 쿼리를 통해 게시글 검색
		Page<Board> allBoard = boardRepository.findAll(boardSearchDTO.specification(), pageable);

		/*
		 * Page 인터페이스의 map 메소드를 사용하면 Page의 내용을 변환할 수 있음. Page<Board>의 각 항목을
		 * BoardDTO::toDTO를 사용하여 변환하고, 변환된 항목들로 새로운 Page<BoardDTO> 객체를 생성
		 */		
		// 조회된 Page<Board> 객체를 Page<BoardDTO> 객체로 변환하여 반환합니다.
		// 각 Board 객체는 BoardDTO::toDTO를 통해 변환됩니다.
		return allBoard.map(BoardDTO::toDTO);

//		return allBoard.stream()  
//				.map(BoardDTO::toDTO)
//                .collect(Collectors.toList());		
		/*
		 * 스트림 객체 얻어오고 엔티티 객체가 저장된 리스트에서 반복적으로 엔티티 객체를 하나씩만 꺼내 엔티티 객체를 dto 변환시켜주는 toDTO
		 * 메소드를 호출하여 dto로 변환 반복적으로 엔티티 객체 리스트를 하나씩 다 꺼내 모두 dto로 변환시킨 후 변환된 모든 dto 객체들을
		 * 새로운 list에 담는 구조
		 */
	}

	// 특정 번호의 게시글을 조회하는 메서드. 조회수 증가 옵션을 선택 / increaseViewCount가 true일 경우 조회수를 증가
	@Override
	public BoardDTO detailBoard(long no, boolean increaseViewCount) {
		// 게시글 번호로 게시글을 찾음. 없을 경우 예외를 발생시킵니다.
		Board board = boardRepository.findById(no).orElseThrow();

		// 조회수를 증가시키는 경우의 로직 / 조회수 증가 옵션이 true라면 조회수를 1 증가시키고 저장
		if (increaseViewCount) {
			board.setReadcount(board.getReadcount() + 1);
			board = boardRepository.save(board); // 조회수가 증가된 Entity를 DB에 반영
		}
		return BoardDTO.toDTO(board); // 조회된 게시글을 DTO 객체로 변환하여 반환
	}
	
	// 특정 번호의 게시글을 수정하는 메서드
	@Override
	public BoardDTO editBoard(long no, BoardDTO editboardDTO) {
		// 수정할 게시글 번호를 기반으로 원본 게시글을 조회 / 없으면 예외를 발생시킵니다. 
		Board origin = boardRepository.findById(no).orElseThrow(); // 수정할 게시글을 조회

		// 원본 게시글의 내용을 입력받은 DTO의 내용으로 변경
		origin.setTitle(editboardDTO.getTitle());
		origin.setWriter(editboardDTO.getWriter());
		origin.setContent(editboardDTO.getContent());
		origin.setPassword(editboardDTO.getPassword());

		Board updatedBoard = boardRepository.save(origin); // 수정된 Entity를 DB에 반영
		return BoardDTO.toDTO(updatedBoard); // 수정된 Entity 객체를 DTO로 변환하여 반환
	}

	// 특정 게시글을 삭제하는 메서드
	@Override
	public void deleteBoard(long no) {
		boardRepository.deleteById(no);
	}
}
