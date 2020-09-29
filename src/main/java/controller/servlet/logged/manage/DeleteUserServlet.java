package controller.servlet.logged.manage;

import bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * @description
 * @date 2020/8/17
 */
@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("DeleteUserServlet:doPost!");
		//设置编码
		req.setCharacterEncoding("utf-8");
		Long userid = Long.valueOf(req.getParameter("delete-userid"));
		String password = req.getParameter("delete-password");
		User user = new User(userid,password);
		//删除
		UserService us = new UserServiceImpl();
		Boolean bool = us.deleteUser(user);
		//返回信息的map
		Map<String, Object> info = new HashMap<String, Object>();
		if (bool!=null){
			if (bool){
				//删除成功
				info.put("delete",true);
				info.put("msg","账号删除成功！");
			}else {
				//账号密码有误
				info.put("delete",false);
				info.put("msg","账号或密码有误，请重试！");
			}
		}else {
			info.put("delete",false);
			info.put("msg","删除账号时出现异常，删除失败，请重试！");
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
