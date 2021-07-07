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
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 寒洲
 * @description 接收客户端上传的图片文件，修改数据库的头像数据
 * @date 2020/8/11
 */
@WebServlet("/UploadServlet")
public class ChangeAvatarServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("UploadServlet:doGet!");
		//设置编码
		request.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");

		String userid = request.getParameter("userid");
		String img = request.getParameter("img");
		//去除ata:image/jpeg;base64,前缀
		int index = img.lastIndexOf(",");
		if (index != -1) {
			img = img.substring(index + 1);
		}
		Base64.Decoder decode = Base64.getDecoder();
		//把base64转成字节码
		byte[] byteImg = decode.decode(img);

		//返回信息的Map
		Map<String, Object> info = new HashMap<>();
		//修改数据库的数据
		UserService us = new UserServiceImpl();
		Boolean bool = us.changeAvatar(Long.valueOf(userid), byteImg);
		info.put("change", bool);
		//将map转为JSON
		ObjectMapper mapper = new ObjectMapper();
		//发送给客户端
		mapper.writeValue(resp.getWriter(), info);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
