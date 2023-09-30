package com.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.board.domain.dto.BoardDTO;
import com.board.domain.dto.ReplyDTO;
import com.board.service.BoardService;
import com.board.service.ReplyService;

@RestController
@RequestMapping("/reply")
public class ReplyController {
	// attr.addFlashAttribute() 리다이렉트시 해당 요청 url 매핑 메소드에 넘어가는 데이터 -> 담아둔 데이터를 받는 메소드는 model 객체나 modelmap 객체를 사용해서 꺼내야 됨.

	private BoardService boardService;
	private ReplyService replyService;

	@Autowired
	public ReplyController(BoardService boardService, ReplyService replyService) {
		this.boardService = boardService;
		this.replyService = replyService;
	}

	// 댓글 등록 핸들러
	@PostMapping("/insert")
	public ReplyDTO insert(@RequestBody ReplyDTO replyDTO) {
		// 해당 게시글의 정보를 가져옴
		BoardDTO board = boardService.detailBoard(replyDTO.getNo(), false);
		// 댓글을 저장하고 결과를 반환
		ReplyDTO reply = replyService.saveReply(board, replyDTO);
		return reply;
	}
	
	// 특정 게시글에 달린 모든 댓글 리스트를 가져오는 기능
	@GetMapping("/list/{boardNo}")
	public List<ReplyDTO> list(@PathVariable long boardNo) {		
		BoardDTO board = boardService.detailBoard(boardNo, false);	
		// 해당 게시글의 모든 댓글 리스트 반환
		return replyService.findAllByBoardNoDesc(board);
	}
	
	// 특정 게시글에 달린 모든 댓글의 개수를 반환하는 메소드
	@GetMapping("/count/{boardNo}")
	public long count(@PathVariable long boardNo) {
		BoardDTO board = boardService.detailBoard(boardNo, false);
		return replyService.countAllBoard(board);
	}
	
	// 댓글 수정 또는 삭제를 위해 비밀번호 입력 페이지로 이동하는 기능
	@GetMapping("/password/{mode:edit|delete}/{no}")
	public ModelAndView replypassword(@PathVariable String mode, @PathVariable long no, ModelAndView mv, @RequestParam long boardNo) {
		mv.addObject("boardno", boardNo);
		mv.addObject("mode", mode);
		mv.addObject("no", no);
		
		mv.setViewName("pages/replypassword"); // 뷰의 이름
		
		// long no : 댓글 pk , long boardNo : 게시글 pk
		// replypassword 이 페이지에서 pk값을 다 갖고 있어야 바로 아래 @PostMapping 에서 호출 될 때 이 pk 값들을 넘겨야 됨. 히든 태그로
		
		return mv;
	}
	
	//댓글 비밀번호 페이지에서 입력한 비밀번호 값 검증
	@PostMapping("/password/{mode:edit|delete}/{no}")
	public ModelAndView replypassword(@PathVariable String mode, @PathVariable long no, @RequestParam long boardNo, @RequestParam String password,
			 ModelAndView mv, RedirectAttributes attr) {
		
		// 해당 댓글 정보 가져오기
		ReplyDTO reply = replyService.detailReply(no);
				
		// 비밀번호 일치 여부에 따라 다른 페이지로 리다이렉트
		// 비밀번호가 일치하면 해당 댓글의 정보를 전달
		if(reply.getPassword().equals(password)) { 
			attr.addFlashAttribute("reply", reply); // 댓글 정보를 FlashAttribute에 저장
			mv.setViewName("redirect:/reply/"+mode); // 댓글 수정/삭제 처리 페이지로 리다이렉트
			return mv;
		} else { // 비밀번호 입력 페이지로 다시 리다이렉트하고, 에러 표시를 합니다.
			attr.addAttribute("boardNo", boardNo);
		    mv.setViewName("redirect:/reply/password/"+mode+"/"+no+"?error");
			return mv;
		}
		
		//return시 수정은 수정 페이지로 get 방식으로 이동 / 삭제 시 get 방식으로 삭제 매핑 하기		
	}
	
	// 댓글 수정 페이지로 이동하는 요청 핸들러
	@GetMapping("/edit") 
	public ModelAndView edit(HttpServletRequest request, ModelAndView mv, Model model) {
		// 수정할 댓글의 정보를 뷰로 전달합니다.
		// Model에서 댓글 정보를 가져옵니다.
		ReplyDTO reply = (ReplyDTO) model.getAttribute("reply");
		
		mv.addObject("boardno", reply.getBoardDTO().getNo()); // 게시글 번호 추가
		mv.addObject("reply", reply); // 댓글 정보 추가
		mv.setViewName("pages/editreply.html");
		return mv;
	}
	
	// 수정된 댓글 정보를 적용하는 메소드
	@PostMapping("/edit") 
	public ModelAndView edit(ReplyDTO replyDTO, ModelAndView mv, RedirectAttributes attr) {
		// 댓글을 수정하고 반환
		ReplyDTO reply = replyService.editBoard(replyDTO.getNo(), replyDTO);
			
		// redirect:/detail 로 리다이렉트 시 게시판 컨트롤러의 해당 매핑된 메소드는 long no, boolean 값을 매개변수 받기 떄문에 이 값을 세팅을 해줘야 되기 때문에 리다이렉트 시 데이터를 넘김
		// 댓글을 수정하고 수정된 게시글의 상세 페이지로 리다이렉트하기 위한 정보 설정
		attr.addAttribute("no", reply.getBoardDTO().getNo()); // 게시글 번호 추가
		attr.addAttribute("increaseViewCount", false); // 조회수 증가를 방지하는 속성을 추가
		
		mv.setViewName("redirect:/detail");
		
		return mv;
	}
	
	// 댓글을 삭제하는 메소드
	@GetMapping("/delete")
	public ModelAndView delete(RedirectAttributes attr, ModelAndView mv, Model model) {
		// Model에서 댓글 정보를 가져옵니다.
		ReplyDTO reply = (ReplyDTO) model.getAttribute("reply");
		// 댓글 삭제 처리
		replyService.deleteReply(reply.getNo()); 
		
		// 댓글이 삭제 처리가 완료된 해당 게시글의 상세 페이지로 리다이렉트하기 위한 정보 설정
		attr.addAttribute("no", reply.getBoardDTO().getNo());
		attr.addAttribute("increaseViewCount", false); 
		
		mv.setViewName("redirect:/detail"); // 게시글의 상세 페이지로 리다이렉트
		return mv;
	}
	
}
