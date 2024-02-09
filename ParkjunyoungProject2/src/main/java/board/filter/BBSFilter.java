package board.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import board.member.model.MemberDTO;
import board.service.MemberService;
import board.util.BoardUtil;
import board.util.JWTOkens;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns ={"/bbs/edit.do","/bbs/delete.do"},asyncSupported = true)
public class BBSFilter implements Filter{

	private HttpServletRequest request;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filter)
			throws IOException, ServletException {
		request=(HttpServletRequest)req;
		
		String accessToken=JWTOkens.getToken(request, req.getServletContext().getInitParameter("ACCESS"));
		
		if(JWTOkens.verifyToken(accessToken,JWTOkens.ACCESS)==JWTOkens.SIGNED) {
			Map<String, Object> payload= JWTOkens.getTokenPayloads(accessToken,JWTOkens.ACCESS);
			ObjectMapper mapper=new ObjectMapper();
			String username=mapper.convertValue(payload.get("username"), String.class);
			if(!username.equals(request.getParameter("username"))) {
				System.out.println("bbsfilter="+payload.get("username").toString());
				resp.setContentType("text/html; charset=UTF-8");
				String status=request.getRequestURI().split("/")[2].equals("delete.do")?"삭제":"수정";
				PrintWriter writer=resp.getWriter();
				writer.write("<script>");
				writer.write("alert('자기가 작성한 글만 "+status+" 가능해요')");
				writer.write("location.replace('/index.do')");
				writer.write("</script>");
				return;
			}
		}else {
			resp.setContentType("text/html; charset=UTF-8");
			PrintWriter writer=resp.getWriter();
			writer.write("<script>");
			writer.write("alert('로그인 후 이용하세요')");
			writer.write("location.replace('/login.do')");
			writer.write("</script>");
			return;
		}
		filter.doFilter(req, resp);
	}

}
