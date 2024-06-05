package board.listener;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import board.service.BBSService;
import board.service.MemberService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener{
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			Context ctx = new InitialContext();
			DataSource source=(DataSource)ctx.lookup(sce.getServletContext().getInitParameter("JNDI_ROOT"));
			sce.getServletContext().setAttribute("DataSource", source);
			sce.getServletContext().setAttribute("service", new MemberService());
			sce.getServletContext().setAttribute("bbsService", new BBSService());
//			ImageVisionAuth.authenticateImplicitWithAdc();
		}
		catch(NamingException e) {e.printStackTrace();}
	}
}
