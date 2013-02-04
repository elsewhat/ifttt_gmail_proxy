package org.elsewhat.ifttt.mail_proxy;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Org_elsewhat_ifttt_mail_proxyServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
