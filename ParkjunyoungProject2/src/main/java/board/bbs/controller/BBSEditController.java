package board.bbs.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import board.bbs.model.BBSDTO;
import board.service.BBSService;
import board.util.BoardUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns ={"/bbs/edit.do"},asyncSupported = true)
@MultipartConfig(maxFileSize = 1024 * 1024 * 5,maxRequestSize = 1024 * 1024 * 5 * 5)
public class BBSEditController extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BBSService service=(BBSService)getServletContext().getAttribute("bbsService");
		String emptyParam=BoardUtil.isEmpty(req.getParameterMap(), "board");
		BBSDTO dto=null;
		ObjectMapper mapper=new ObjectMapper();
		if(emptyParam==null) {
			dto=service.bbsUpdate(req);
		}else {
			Map<String, String> error=new HashMap<String, String>();
			error.put("error", emptyParam);
			mapper.writeValue(resp.getWriter(),error);
			return;
		}
		mapper.writeValue(resp.getWriter(),dto);
	}
}
