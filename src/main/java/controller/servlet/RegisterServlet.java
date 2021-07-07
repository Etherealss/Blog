package controller.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import bean.User;
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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 寒洲
 * @description 注册后台逻辑
 * @date 2020/8/3
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("用户注册！");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");

		/*
		 * 下面这个是储存注册结果的map
		 * 按道理是一定会注册成功的，但是有可能因为意外情况没能注册成功
		 * 比如下面遇到异常
		 * 所以这里判断一下注册结果并且反馈给客户端
		 */
		Map<String, Object> info = new HashMap<>();

		/*
		 * 在日期转型时需要处理异常
		 * 为了当转型失败的时候后续代码都不执行，不往map添加新信息
		 */
		// 我把后续所有的代码都包裹在try-catch中
		try {
			//获取请求参数
			String nickname = req.getParameter("register-nickname");
			Long id = Long.valueOf(req.getParameter("register-userid"));
			String password = req.getParameter("register-pw1");
			//将获取的生日字符串转换为日期类型
			Date birthday = new SimpleDateFormat("yyyy-MM-dd").
					parse(req.getParameter("register-birthday"));

			String sex_str = req.getParameter("register-sex");
			//判断性别，小哥哥为true
			Boolean sex = "male".equals(sex_str);
			//根据性别加载默认头像
			InputStream fis = null;
			if (sex) {
				//小哥哥头像
				//获取webapp下的images图片数据
				fis = this.getServletContext().getResourceAsStream("images/boy.png");
			} else {
				//小姐姐头像
				fis = this.getServletContext().getResourceAsStream("images/girl.png");
			}
			// 封装到user对象中
			// 通过User.getbytes(fis)将InputStream转为byte[]，以储存在数据库中
			User user = new User(id, password, nickname, User.getbytes(fis), sex, birthday);
			//提交用户信息
			UserService us = new UserServiceImpl();
			if (us.register(user)) {
				//注册成功
				info.put("register", true);
				info.put("msg", "恭喜这位小可爱注册成功！");
			} else {
				//注册失败
				info.put("register", false);
				info.put("msg", "哎呀！注册失败了，请重试！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//注册失败
			info.put("register", false);
			info.put("msg", "出现异常，注册失败，请重试！");
		}
		//将map转为JSON
		ObjectMapper mapper = new ObjectMapper();
		//发送给客户端
		mapper.writeValue(resp.getWriter(), info);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
