package board.bbs.controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import board.bbs.model.BBSDTO;
import board.service.BBSService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/bbs/delete.do")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5,maxRequestSize = 1024 * 1024 * 5 * 5)
public class BBSDeleteController extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BBSService service=(BBSService)getServletContext().getAttribute("bbsService");
		BBSDTO dto=service.bbsDelete(req);
		System.out.println("deleteController:"+dto);
		ObjectMapper mapper=new ObjectMapper();
		mapper.writeValue(resp.getWriter(),dto);
	}
}
