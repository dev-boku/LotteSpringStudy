package com.springbook.view.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.springbook.biz.board.BoardService;
import com.springbook.biz.board.BoardVO;
import com.springbook.biz.user.UserVO;
import com.springbook.biz.user.impl.UserDAO;

public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("EUC-KR");
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"));
		System.out.println(path);
		
		if (path.equals("/login.do")) {
			System.out.println("로그인 처리");
			
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			UserVO vo = new UserVO();
			vo.setId(id);
			vo.setPassword(password);
			
			UserDAO userDAO = new UserDAO();
			UserVO user = userDAO.getUser(vo);
			
			if (user != null) {
				response.sendRedirect("getBoardList.do");
			} else {
				response.sendRedirect("login.jsp");
			}
			
		} else if (path.equals("/logout.do")) {
			System.out.println("로그아웃 처리");
			
			HttpSession session = request.getSession();
			session.invalidate();

			response.sendRedirect("login.jsp");
			
		} else if (path.equals("/insertBoard.do")) {
			System.out.println("글 등록 처리");
			
			request.setCharacterEncoding("EUC-KR");
			String title = request.getParameter("title");
			String writer = request.getParameter("writer");
			String content = request.getParameter("content");
			
			BoardVO vo = new BoardVO();
			vo.setTitle(title);
			vo.setWriter(writer);
			vo.setContent(content);
			
			AbstractApplicationContext container = new GenericXmlApplicationContext("applicationContext.xml");
			BoardService boardService = (BoardService) container.getBean("boardService");
			
			boardService.insertBoard(vo);
			
			response.sendRedirect("getBoardList.do");
			
		} else if (path.equals("/updateBoard.do")) {
			System.out.println("글 수정 처리");
			
			request.setCharacterEncoding("EUC-KR");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String seq = request.getParameter("seq");
			
			BoardVO vo = new BoardVO();
			vo.setTitle(title);
			vo.setContent(content);
			vo.setSeq(Integer.parseInt(seq));
			
			AbstractApplicationContext container = new GenericXmlApplicationContext("applicationContext.xml");
			BoardService boardService = (BoardService) container.getBean("boardService");
			boardService.updateBoard(vo);
			
			response.sendRedirect("getBoardList.do");
			
		} else if (path.equals("/deleteBoard.do")) {
			System.out.println("글 삭제 처리");
			
			String seq = request.getParameter("seq");
			
			BoardVO vo = new BoardVO();
			vo.setSeq(Integer.parseInt(seq));
			
			AbstractApplicationContext container = new GenericXmlApplicationContext("applicationContext.xml");
			BoardService boardService = (BoardService) container.getBean("boardService");
			boardService.deleteBoard(vo);
			
			response.sendRedirect("getBoardList.do");
			
			
		} else if (path.equals("/getBoard.do")) {
			System.out.println("글 상세 조회 처리");
			
			String seq = request.getParameter("seq");

			BoardVO vo = new BoardVO();
			vo.setSeq(Integer.parseInt(seq));
			
			AbstractApplicationContext container = new GenericXmlApplicationContext("applicationContext.xml");
			BoardService boardService = (BoardService) container.getBean("boardService");
			BoardVO board = boardService.getBoard(vo);
			
			HttpSession session = request.getSession();
			session.setAttribute("board", board);
			response.sendRedirect("getBoard.jsp");
			
		} else if (path.equals("/getBoardList.do")) {
			System.out.println("글 목록 검색 처리");
			
			BoardVO vo = new BoardVO();
			AbstractApplicationContext container = new GenericXmlApplicationContext("applicationContext.xml");
			BoardService boardService = (BoardService) container.getBean("boardService");
			List<BoardVO> boardList = boardService.getBoardList(vo);
			
			HttpSession session = request.getSession();
			session.setAttribute("boardList", boardList);
			response.sendRedirect("getBoardList.jsp");
		}
 	}
}
