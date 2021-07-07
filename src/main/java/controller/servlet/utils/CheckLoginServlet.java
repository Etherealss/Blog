package controller.servlet.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 寒洲
 * @description 检查是否登录
 * @date 2020/8/18
 */
@WebServlet("/CheckLoginServlet")
public class CheckLoginServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.print("CheckLoginServlet 检查是否登录：");
		Object userid = req.getSession().getAttribute("userid");
		if (userid == null) {
			//未登录，发送信息给前端
			/*
			这里本来是用response的重定向直接去往warning.html的
			但是用ajax访问后端不能用这种方式跳转，要用ajax跳转
			 */
			resp.getWriter().println("false");
			logger.debug("未登录");
		}else{
			//已登录，发送userid给前端验证
			resp.getWriter().println(userid);
			logger.debug("已登录：" + userid);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
}
