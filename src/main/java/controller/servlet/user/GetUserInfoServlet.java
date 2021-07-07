package controller.servlet.user;

import bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author 寒洲
 * @description 获取用户的个人信息
 * @date 2020/8/18
 */
@WebServlet("/GetUserInfoServlet")
public class GetUserInfoServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("获取用户的个人信息");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");
		//获取请求参数
		Long id = Long.valueOf(req.getParameter("userid"));
		//根据id获取用户信息
		UserService us = new UserServiceImpl();
		User user = us.getUserInfo(id);
		//返回信息的map
		Map<String, Object> info = new HashMap<>();
		if (user != null) {
			info.put("get", true);
			info.put("user", user);
		} else {
			info.put("get", false);
		}
		//将map转为JSON
		ObjectMapper mapper = new ObjectMapper();
		//发送给客户端
		mapper.writeValue(resp.getOutputStream(), info);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
}
