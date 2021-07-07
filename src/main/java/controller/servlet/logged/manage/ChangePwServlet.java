package controller.servlet.logged.manage;

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
 * @description 修改用户密码的后端操作
 * @date 2020/8/9
 */
@WebServlet("/ChangePwServlet")
public class ChangePwServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");


	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("修改密码");
		//设置编码
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json;charset=utf-8");
		//获取参数
		String userid = req.getParameter("userid");
		String originalPw = req.getParameter("original-pw");
		String newPw = req.getParameter("new-pw");
		//调用下面的方法进行数据库的操作
		UserService us = new UserServiceImpl();
		Boolean update = us.changePw(Long.valueOf(userid), originalPw, newPw);

		Map<String, Object> info = new HashMap<>();
		if (update!=null){
			if (update) {
				//修改成功
				info.put("change", true);
				info.put("msg", "密码修改成功！");
			} else {
				//密码错误，修改失败
				info.put("change", false);
				info.put("msg", "原密码错误，修改失败！");
			}
		}else{
			//遇到意外，修改失败
			info.put("change", false);
			info.put("msg", "用户密码修改失败！请稍后重试！");
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
