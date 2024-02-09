package board.member.controller;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import board.member.model.MemberDTO;
import board.service.MemberService;
import board.util.BoardUtil;
import board.util.JWTOkens;
import board.util.PathKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/edit.do")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5,maxRequestSize = 1024 * 1024 * 5)
public class EditController extends HttpServlet{
	private MemberService service;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(BoardUtil.getPath(PathKeys.EDIT_VIEW.getValue())).forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		service=(MemberService)getServletContext().getAttribute("service");
		System.out.println("controller:"+req.getParameter("savedFileName"));
		System.out.println("controller:"+req.getParameter("username"));
		MemberDTO dto=service.memberUpdate(req);
		if(dto==null) {
			doGet(req, resp);
			return;
		}
		resp.sendRedirect(BoardUtil.getPath(PathKeys.INDEX.getValue()));
	}
}
