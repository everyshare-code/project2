package board.member.controller;

import java.io.IOException;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import board.member.model.MemberDAO;
import board.member.model.MemberDTO;
import board.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/check-id.do")
public class CheckIdController extends HttpServlet{
	
	private MemberService service;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("요청");
		service=(MemberService)getServletContext().getAttribute("service");
		
		MemberDTO dto=service.memberSelectOne(req);
		System.out.println(dto);
		ObjectMapper mapper=new ObjectMapper();
		mapper.writeValue(resp.getWriter(), dto);
		
	}
}
