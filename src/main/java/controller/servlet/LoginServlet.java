package controller.servlet;

import bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.impl.UserServiceImpl;
import utils.CookieUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 登录
 * @Author 寒洲
 * @Date 2020/7/29
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("用户登录");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");
		//获取请求参数
		Long id = Long.valueOf(req.getParameter("login-userid"));
		String password = req.getParameter("login-password");

		//通过查询数据库获取登录结果
		UserService us = new UserServiceImpl();
		Map<String, Object> info = us.login(id, password);
		//判断是否登录成功，如果成功则将用户信息存入cookie
		if (Boolean.parseBoolean(info.get("login").toString())){
			//获取user
			User user = (User) info.get("user");
			//设置cookie，并通过response保存cookie对象
			resp.addCookie(CookieUtils.setUserCookie(user));
			//移除user，不发送给客户端。客户端可以通过Cookie获取用户信息
			info.remove("user");
			//设置session，用于检验登录状态
			req.getSession().setAttribute("userid", id);
		}

		//将map转为JSON
		ObjectMapper mapper = new ObjectMapper();
		//发送给客户端
		mapper.writeValue(resp.getOutputStream(), info);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
