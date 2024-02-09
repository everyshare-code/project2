package board.service;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


import board.bbs.model.BBSDAO;
import board.bbs.model.BBSDTO;
import board.member.model.MemberDAO;
import board.member.model.MemberDTO;
import board.util.JWTOkens;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberService {
	
	
	public MemberDTO memberInsert(HttpServletRequest req) throws IOException, ServletException {
		MemberDAO dao=new MemberDAO(req);
		String username=req.getParameter("username");
		String[] fileInfo=null;
		String filename=null;
		String contentType=null;
		if(req.getParameter("encoded-file")!=null&&!req.getParameter("encoded-file").isBlank()) {
			fileInfo=req.getParameter("encoded-file").split(",");
			filename=fileInfo[1];
			contentType=fileInfo[0];
		}
		
		String password=req.getParameter("password");
		String gender=req.getParameter("gender");
		String inters=Arrays.stream(req.getParameterValues("inters")).collect(Collectors.joining(","));
		String education=req.getParameter("education");
		String self_intro=req.getParameter("self-intro");
		
		MemberDTO dto=new MemberDTO(username, password, gender, inters, 
								education, self_intro, filename, 
								new Date(new java.util.Date().getTime()).toString(),
								contentType,null);
		int affected=dao.insert(dto);
		dao.close();
		return affected==0?null:dto;
	}
	
	public MemberDTO memberSelectOne(HttpServletRequest req) {
		MemberDAO dao=new MemberDAO(req);
		MemberDTO dto=dao.selectOne(req.getParameter("username")==null?
				req.getAttribute("username").toString():req.getParameter("username"));
		if(dto!=null) {
			BBSDAO bbsDao=new BBSDAO(req);
			dto.setBbs(bbsDao.selectAllbyUsername(dto));
			bbsDao.close();
		}
		dao.close();
		return dto;
	}
	
	public MemberDTO memberUpdate(HttpServletRequest req) {
		MemberDAO dao=new MemberDAO(req);
		
		String username=req.getParameter("username");
		String[] fileInfo=null;
		String filename=null;
		String contentType=null;
		if(req.getParameter("encoded-file")!=null&&!req.getParameter("encoded-file").isEmpty()) {
			fileInfo=req.getParameter("encoded-file").split(",");
			filename=fileInfo[1];
			contentType=fileInfo[0];
		}
//		else {
//			fileInfo=req.getParameter("savedFile").split(",");
//			filename=fileInfo[1];
//			contentType=fileInfo[0];
//		}
		String password=req.getParameter("password");
		String inters=Arrays.stream(req.getParameterValues("inters")).collect(Collectors.joining(","));
		String education=req.getParameter("education");
		String self_intro=req.getParameter("self-intro");
		
		MemberDTO dto=new MemberDTO();
		dto.setSelf_image(filename);
		dto.setPassword(password);
		dto.setInters(inters);
		dto.setEducation(education);
		dto.setSelf_intro(self_intro);
		dto.setUsername(username);
		dto.setContent_type(contentType);
		
		int affected=dao.update(dto);
		dao.close();
		System.out.println("affected="+affected);
		return affected==0?null:dto;
	}
	
	public boolean isMember(HttpServletRequest req,MemberDTO dto) {
		boolean isMember=false;
		MemberDAO dao=new MemberDAO(req);
		int count=dao.selectCount(dto);
		dao.close();
		if(count==1) isMember=true;
		System.out.println("isMember="+isMember);
		return isMember;
	}
	
	public void issueTokens(HttpServletRequest req,HttpServletResponse res,int status) {
		String username=req.getAttribute("username").toString();
		
		Cookie accessCookie=null;
		Cookie refreshCookie=null;
		Map<String,Object> payload=new HashMap<>();
		payload.put("username", username);
		String accessToken=JWTOkens.createToken(username, payload, 1000*60*30, JWTOkens.ACCESS);
		accessCookie=new Cookie(req.getServletContext().getInitParameter("ACCESS"),accessToken);
		accessCookie.setHttpOnly(true);
		accessCookie.setSecure(true);
		accessCookie.setMaxAge(60*60);
		
		res.addCookie(accessCookie);
		
		if(status==JWTOkens.ACCESS) {
			String refreshToken=JWTOkens.createToken(username, payload, 1000*60*60*24, JWTOkens.REFRESH);
			refreshCookie=new Cookie(req.getServletContext().getInitParameter("REFRESH"),refreshToken);
			refreshCookie.setHttpOnly(true);
			refreshCookie.setSecure(true);
			refreshCookie.setMaxAge(60*60*24);
			res.addCookie(refreshCookie);
		}
		
	}
	
	
}
