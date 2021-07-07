package controller.servlet.logged;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 寒洲
 * @description 用户退出登录
 * @date 2020/8/18
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("用户退出登录");
		//获取退出登录的userid
		Object userid = req.getSession().getAttribute("userid");
		UserService us = new UserServiceImpl();
		//修改数据库的在线状态
		us.logout(Long.valueOf(String.valueOf(userid)));

		//删除session
		logger.trace("删除了session");
		req.getSession().invalidate();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
}
