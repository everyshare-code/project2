package board.member.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;

import board.member.model.MemberDAO;
import board.member.model.MemberDTO;
import board.service.MemberService;
import board.util.BoardUtil;
import board.util.JWTOkens;
import board.util.PathKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/login.do")
public class LoginController extends HttpServlet{
	private MemberService service;
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(BoardUtil.getPath(PathKeys.LOGIN_VIEW.getValue()))
			.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		service=(MemberService)req.getServletContext().getAttribute("service");
		MemberDTO dto=null;
		if(req.getAttribute("username")==null) {
			dto=new MemberDTO();
			dto.setUsername(req.getParameter("username"));
			dto.setPassword(req.getParameter("password"));
			if(!service.isMember(req, dto)) {
				req.setAttribute("LOGIN_ERROR", "아이디 비밀번호 확인");
				doGet(req, resp);
				return;
			}
			req.setAttribute("username",dto.getUsername());
		}
		
		service.issueTokens(req,resp,JWTOkens.ACCESS);
	
		resp.sendRedirect(req.getContextPath()
				+BoardUtil.getPath(PathKeys.INDEX.getValue()));
	}
}
