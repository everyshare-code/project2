package board.member.controller;

import java.io.IOException;

import board.util.BoardUtil;
import board.util.JWTOkens;
import board.util.PathKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/logout.do")
public class LogoutController extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JWTOkens.removeToken(req, resp);
		resp.sendRedirect(req.getContextPath()+BoardUtil.getPath(PathKeys.INDEX.getValue()));
	}
}
