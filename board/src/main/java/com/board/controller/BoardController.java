package com.board.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.board.domain.dto.BoardDTO;
import com.board.domain.dto.BoardPaginationDTO;
import com.board.domain.dto.BoardSearchDTO;
import com.board.service.BoardService;

@Controller
public class BoardController {
	// RedirectAttributes 객체 -> 서버가 클라이언트에게 리다이렉트 요청 명령문 + RedirectAttributes 객체에 담아진 추가 데이터 보냄 
	// 리다이렉트시에 클라이언트가 서버에 추가적으로 보내는 데이터 // 클라이언트는 다시 리다이렉트 요청 시에 RedirectAttributes 객체에 담겨진 데이터를 포함하여 서버에 요청 함.
		
	private BoardService boardService;
	
	@Autowired
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	// 메인 페이지 요청 핸들러. 게시판 리스트를 보여줍니다.
	@RequestMapping("/")
	public String list(Model model, BoardSearchDTO boardSearchDTO, 
			           @PageableDefault(page = 0, size = 10) // 페이징 기본값 설정
					   @SortDefault(sort = "no", direction = Sort.Direction.ASC) // 정렬 기본값 설정
	                   Pageable pageable) {
		
		Page<BoardDTO> list = boardService.findAllBoard(boardSearchDTO, pageable);
		model.addAttribute("list", list); // 게시글 리스트를 Model에 추가
		model.addAttribute("pagination", new BoardPaginationDTO(list)); // 페이징 정보를 Model에 추가
		return "pages/list";
	}
	
	// 게시글 작성 페이지 요청시 작성 페이지로 이동하는 핸들러
	@GetMapping("/write")
	public String write() {
		return "pages/write";
	}
	
	// 게시글 작성 데이터를 처리하는 메서드
	@PostMapping("/write") // 게시글 등록
	public String write(BoardDTO boardDTO, RedirectAttributes attr) { // 사용자가 입력한 게시글 정보를 담은 DTO, 리다이렉트 시 데이터 전달을 위한 객체
		BoardDTO board = boardService.saveBoard(boardDTO); // 게시글을 저장하고 저장된 게시글 정보를 반환
//		return "redirect:/"; // 등록 후 리스트 페이지로 이동 -> 바로 게시글 등록 후에 조회수 증가 X
		
		// 조회수 증가 방지 플래그(속성) 설정
		attr.addAttribute("increaseViewCount", false); // 게시글 등혹 후 바로 조회수 증가 X (@GetMapping("/detail") 호출 시 boolean increaseViewCount 값 false로 저장)
		// 작성한 게시글의 상세 페이지로 리다이렉트
		return "redirect:detail?no="+ board.getNo(); // 위 로직 없이 등록 후 바로 해당 게시글 상세보기 페이지로 -> 다만 조회수가 증가하게 됨.
	}
	
	// 게시글 상세보기 페이지 요청시 뷰 반환하는 메서드
	@GetMapping("/detail") // 게시글 상세보기
	public String detail(@RequestParam long no, @RequestParam(required = false, defaultValue = "true") boolean increaseViewCount, Model model) {
		BoardDTO board = boardService.detailBoard(no, increaseViewCount);
		model.addAttribute("board", board);
		return "pages/detail";
	}
	
	
	// 비밀번호 로직이 추가되었으므로 단순하게 번호를 받는 것이 아닌 FlashMap을 수신하는 코드로 변경
	// 게시글 수정 페이지로 이동하는 메서드
	@GetMapping("/edit") // 게시글 데이터 수정 요청시 수정 페이지로 이동
	public String edit(HttpServletRequest request, Model model) {
		//FlashMap 수신 코드
		//세션에서 FlashMap을 통해 전달된 데이터를 가져옵니다.
		Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);
		//FlashMap 데이터가 없으면 예외 발생
		if(map == null) throw new RuntimeException("권한 없음");
		//FlashMap에서 게시글 번호 가져오기
		Long no = (Long)map.get("no");		
		
		// 게시글 상세 조회 (조회수 증가 X)
		BoardDTO board = boardService.detailBoard(no, false);
		model.addAttribute("board", board);
		return "pages/edit";
	}
	
	// 게시글 수정 데이터를 처리하는 메서드
	@PostMapping("/edit") // 게시글 수정한 데이터 적용
	public String edit(BoardDTO editboardDTO, RedirectAttributes attr) { // 리다이렉트 속성을 파라미터로 받습니다.
		BoardDTO board = boardService.editBoard(editboardDTO.getNo(), editboardDTO);
		// 리다이렉트시 전달할 게시글 번호와 조회수 증가 방지를 위한 속성 데이터 추가
		attr.addAttribute("no", board.getNo());		
		attr.addAttribute("increaseViewCount", false); // 게시글 데이터 수정 시 조회수 증가 X	
		return "redirect:detail"; // 수정된 게시글 상세보기 페이지로 리다이렉트	
	}
	
	// 비밀번호 로직이 추가되었으므로 단순하게 번호를 받는 것이 아닌 FlashMap을 수신하는 코드로 변경
	// 게시글 삭제 요청을 처리하는 메서드
	@GetMapping("/delete")
	public String delete(HttpServletRequest request) { // HTTP 요청 정보를 담은 객체
		//FlashMap 수신 코드
		//세션에서 FlashMap 정보를 받아옵니다. / FlashMap에서 데이터 추출
		Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);
		if(map == null) throw new RuntimeException("권한 없음");
		// 게시글 번호 추출
		Long no = (Long)map.get("no");	
				
		boardService.deleteBoard(no); // 게시글 삭제
		return "redirect:/"; // 메인 페이지(게시글 리스트)로 리다이렉트
	}
	
	//게시판 수정/삭제시 해당 게시판의 비밀번호를 입력받는 페이지로 이동되는 메소드
	//비밀번호 검사 매핑 추가
	//- /password/모드/번호
	//- 모드는 반드시 edit 또는 delete 중 하나가 오도록 정규식 검사 처리
	@GetMapping("/password/{mode:edit|delete}/{no}")
	public String password(@PathVariable String mode, @PathVariable long no, Model model) { // 비밀번호 체크 요청 모드 (edit or delete), 비밀번호 체크할 게시글 번호
		// model에 비밀번호 체크 모드와 게시글 번호 추가
		model.addAttribute("mode", mode);
		model.addAttribute("no", no);
		return "pages/password";
	}
		
	// 비밀번호 검증 후 게시글 수정/삭제를 수행하는 요청 핸들러
	@PostMapping("/password/{mode:edit|delete}/{no}")
	public String password(@PathVariable String mode, @PathVariable long no, @RequestParam String password,
										RedirectAttributes attr) {
		// 게시글 상세 조회 (조회수 증가 X)
		BoardDTO board = boardService.detailBoard(no, false);
		
		// 입력된 비밀번호와 저장된 비밀번호 비교
		// 사용자가 입력한 비밀번호와 저장된 비밀번호가 일치하는 경우
		if(board.getPassword().equals(password)) {//비밀번호 일치 - 다음 단계로 이동
			//이동은 하지만 번호를 알 수가 없으며, 이 페이지를 통과했다는 보증이 필요하므로 FlashAttribute를 사용
			//FlashAttribute는 세션을 사용하며 요청 후 소멸하므로 현재 상태에서 유용하게 사용할 수 있는 값
			//-> 하지만 edit와 delete의 로직을 변경해야함
			attr.addFlashAttribute("no", no); // 게시글 번호를 FlashAttribute에 추가
			// 모드에 따라 게시글 수정/삭제 페이지로 리다이렉트
			return "redirect:/"+mode; // 해당 모드(edit or delete)의 GET 요청 처리 메서드로 리다이렉트
		}
		else { // 비밀번호 불일치 시 에러와 함께 비밀번호 입력 페이지로 다시 리다이렉트
			return "redirect:/password/"+mode+"/"+no+"?error"; // error 플래그 설정
		}
	}
		
}
