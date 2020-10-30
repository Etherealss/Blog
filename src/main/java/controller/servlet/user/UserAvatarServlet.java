package controller.servlet.user;

import bean.User;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @description 获取用户头像并输出
 * @author 寒洲
 * @date 2020/8/6
 */
@WebServlet("/UserAvatarServlet")
public class UserAvatarServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("UserAvatarServlet:获取用户头像并输出!");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//获取请求参数
		String id_str = req.getParameter("id");
		System.out.println(id_str);
		byte[] avatarData=null;
		if (id_str.equals("cancelled")){
			//显示“已注销”头像
			InputStream fis = this.getServletContext().getResourceAsStream("images/cancelled.png");
			try {
				avatarData = User.getbytes(fis);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			//显示用户头像
			Long id = Long.valueOf(id_str);
			//获取头像数据
			UserService us = new UserServiceImpl();
			avatarData = us.getUserAvatar(id);
		}
		if (avatarData!=null){
			//输出头像数据
			ServletOutputStream sos = resp.getOutputStream();
			sos.write(avatarData);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
