package board.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import board.member.model.MemberDAO;
import board.member.model.MemberDTO;
import board.service.MemberService;
import board.util.BoardUtil;
import board.util.JWTOkens;
import board.util.PathKeys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter({"/index.do","/edit.do","/login.do"})
public class LoginFilter implements Filter{

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filter)
			throws IOException, ServletException {
		
		request=(HttpServletRequest)req;
		response=(HttpServletResponse)resp;
		MemberService service=(MemberService)request.getServletContext().getAttribute("service");
		String accessToken=JWTOkens.getToken(request, req.getServletContext().getInitParameter("ACCESS"));
		String refreshToken=JWTOkens.getToken(request, req.getServletContext().getInitParameter("REFRESH"));
		System.out.println("accessToken="+accessToken);
		System.out.println("refreshToken="+refreshToken);
		if(!accessToken.isEmpty()) {
			int accessVerifyStatus=JWTOkens.verifyToken(accessToken,JWTOkens.ACCESS);
			switch (accessVerifyStatus) {
				case JWTOkens.SIGNED:
				case JWTOkens.EXPIRED:
					int refreshVerifyStatus=JWTOkens.verifyToken(refreshToken,JWTOkens.REFRESH);
					switch(refreshVerifyStatus) {
						case JWTOkens.EXPIRED:
							service.issueTokens(request,response,JWTOkens.REFRESH);
						case JWTOkens.SIGNED:
							setUserInfo(refreshToken, request, response, service);
							break;
						case JWTOkens.UNSIGNED:
							JWTOkens.removeToken(request, response);
							loginFail(resp);
					}
					break;
				case JWTOkens.UNSIGNED:{
					JWTOkens.removeToken(request, response);
					loginFail(resp);
				}
			}		
		}
		filter.doFilter(req, resp);
	}
	
	public void loginFail(ServletResponse resp) throws IOException {
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter writer=resp.getWriter();
		writer.write("<script>");
		writer.write("alert('로그인 후 이용하세요')");
		writer.write("location.replace('/login.do')");
		writer.write("<script>");
	}
	
	public void setUserInfo(String refreshToken,HttpServletRequest request,HttpServletResponse response,MemberService service) {
		Map<String,Object> payload=JWTOkens.getTokenPayloads(refreshToken, JWTOkens.REFRESH);
		request.setAttribute("username", payload.get("username").toString());
		MemberDTO dto=service.memberSelectOne(request);
		request.setAttribute("member", dto);
	}
	

}
