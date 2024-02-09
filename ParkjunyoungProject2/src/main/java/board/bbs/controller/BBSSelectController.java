package board.bbs.controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import board.bbs.model.BBSDTO;
import board.service.BBSService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/bbs/select.do")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5,maxRequestSize = 1024 * 1024 * 5 * 5)
public class BBSSelectController extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BBSService service=(BBSService)getServletContext().getAttribute("bbsService");
		List<BBSDTO> list=service.bbsSelect(req);
		ObjectMapper mapper=new ObjectMapper();
		mapper.writeValue(resp.getWriter(), list);
	}
	
}
