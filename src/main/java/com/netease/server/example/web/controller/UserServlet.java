package com.netease.server.example.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 *
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 4607606190625660785L;

	/**
	 * Logger for this class.
	 */
	private static Logger logger = Logger.getLogger(UserServlet.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.info("UserServlet post method is invoked.");
		response.setContentType("text/html;charset=UTF-8");
		process(request, response);
	}

	protected void process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = null;
		String userName = request.getParameter("userName");
		String userPassword = request.getParameter("userPassword");

//		HttpSession session = request.getSession();
//		String name = (String) session.getAttribute("userName");
//
//		if (name != null) {
//			System.out.println("second login: " + name);
//		}
//
//		session.setAttribute("userName", userName);
	    logger.info("user name in request is " + userName);
		logger.info("pwd in request is " + userPassword);
		Cookie userNameCookie = new Cookie("userName", userName);
		Cookie pwdCookie = new Cookie("pwd", userPassword);

		userNameCookie.setMaxAge(10 * 60);
		pwdCookie.setMaxAge(10 * 60);

		response.addCookie(userNameCookie);
		response.addCookie(pwdCookie);

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			logger.info("cookie not null");
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("userName")) {
					userName = cookie.getValue();
					logger.info("username in cookie is " + userName);
				}
				if (cookie.getName().equals("pwd")) {
					userPassword = cookie.getValue();
					logger.info("pwd in cookie is " + userPassword);
				}
			}
		}

		try {
			if (userName.equals("123") && userPassword.equals("123")) {
				PrintWriter writer = response.getWriter();
				writer.println("<html>");
				writer.println("<head><title>用户中心</title></head>");
				writer.println("<body>");
				writer.println("<p>用户名：" + userName + "</p>");
				writer.println("<p>用户密码：" + userPassword + "</p>");
				writer.println("</body>");
				writer.println("</html>");
				writer.close();
			} else {
				dispatcher = request.getRequestDispatcher("/error.html");
				dispatcher.forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			dispatcher = request.getRequestDispatcher("/error.html");
			dispatcher.forward(request, response);
		}

	}
}
