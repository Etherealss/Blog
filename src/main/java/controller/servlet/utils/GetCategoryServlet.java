package controller.servlet.utils;

import bean.Blog;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.BlogService;
import service.impl.BlogServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 寒洲
 * @description 获取博客分类名
 * @date 2020/8/19
 */
@WebServlet("/GetCategoryServlet")
public class GetCategoryServlet extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("GetCategoryServlet:获取博客分类名");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");
		BlogService bs = new BlogServiceImpl();
		List<String> category = bs.getCategory();
		Map<String, Object> data = new HashMap<>();
		data.put("category",category);
		//将map转为JSON
		ObjectMapper mapper = new ObjectMapper();
		//发送给客户端
		mapper.writeValue(resp.getWriter(), data);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
}
