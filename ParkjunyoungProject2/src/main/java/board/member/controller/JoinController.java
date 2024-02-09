package board.member.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import board.member.model.MemberDAO;
import board.member.model.MemberDTO;
import board.service.MemberService;
import board.util.BoardUtil;
import board.util.PathKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/join.do")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5,maxRequestSize = 1024 * 1024 * 5)
public class JoinController extends HttpServlet{
	private MemberService service;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(BoardUtil.getPath(PathKeys.JOIN_VIEW.getValue()))
			.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service=(MemberService)getServletContext().getAttribute("service");
		String emptyParam=BoardUtil.isEmpty(req.getParameterMap(), "MEMBER");
		MemberDTO dto=service.memberSelectOne(req);
		if(emptyParam!=null) {
			req.setAttribute("JOIN_ERROR", emptyParam+"는 필수 항목");
			req.getRequestDispatcher(BoardUtil.getPath(PathKeys.JOIN_VIEW.getValue()))
			.forward(req, resp);
			return;
		}else if(dto!=null) {
			req.setAttribute("JOIN_ERROR", "중복된 아이디");
			req.getRequestDispatcher(BoardUtil.getPath(PathKeys.JOIN_VIEW.getValue()))
			.forward(req, resp);
			return;
		}
		
		dto=service.memberInsert(req);
		if(dto!=null) {
			req.setAttribute("username", dto.getUsername());
			req.getRequestDispatcher(BoardUtil.getPath(PathKeys.LOGIN.getValue())).forward(req, resp);
		}else {
			req.setAttribute("JOIN_ERROR", "failed");
			req.getRequestDispatcher(BoardUtil.getPath(PathKeys.JOIN_VIEW.getValue()))
			.forward(req, resp);
		}
		
		
	}
}
