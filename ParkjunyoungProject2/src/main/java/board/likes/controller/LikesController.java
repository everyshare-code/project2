package board.likes.controller;

import java.io.IOException;


import board.service.BBSService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/bbs/likes/insert","/bbs/likes/delete"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5,maxRequestSize = 1024 * 1024 * 5 * 5)
public class LikesController extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BBSService service=(BBSService)getServletContext().getAttribute("bbsService");
		int success=0;
		
		if(req.getRequestURI().equals("/bbs/likes/insert")) {
			success=service.likeUp(req);
		}else {
			success=service.likeDown(req);
		}
		resp.setContentType("text/plain; charset=UTF-8");
		resp.getWriter().write(String.valueOf(success));
	}
}
