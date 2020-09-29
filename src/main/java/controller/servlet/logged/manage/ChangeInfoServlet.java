package controller.servlet.logged.manage;

import com.fasterxml.jackson.databind.ObjectMapper;
import bean.User;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import dao.utils.JDBCUtils;
import service.UserService;
import service.impl.UserServiceImpl;
import utils.CookieUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 寒洲
 * @description 修改用户信息的后端操作
 * @date 2020/8/7
 */
@WebServlet("/ChangeInfoServlet")
public class ChangeInfoServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("ManageInfoServlet:doGet!");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");
		//获取请求参数
		Long originalId = Long.valueOf(req.getParameter("original-id"));
		String nickname = req.getParameter("nickname");
		System.out.println("nickname = " + nickname);
		Long id = Long.valueOf(req.getParameter("userid"));
		Date birthday;
		//为了当转换失败的时候后续代码都不执行，我把后续所有的代码都包裹在try-catch中
		try {
			//将获取的生日字符串转换为日期类型
			birthday = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("birthday"));

			String sex_str = req.getParameter("sex");
			//判断性别，小哥哥为true
			Boolean sex = "male".equals(sex_str);
			User user = new User(id, null, nickname, null, sex, birthday);
			resp.addCookie(CookieUtils.setUserCookie(user));
			Map<String, Object> info = new HashMap<>();
			//修改信息，传入修改结果
			UserService us = new UserServiceImpl();
			Boolean bool = us.changeInfo(user, originalId);
			info.put("change", bool);
			//将map转为JSON
			ObjectMapper mapper = new ObjectMapper();
			//发送给客户端
			mapper.writeValue(resp.getWriter(), info);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
