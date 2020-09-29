package controller.servlet.logged;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 寒洲
 * @description
 * @date 2020/8/17
 */
@WebServlet("/ChangeBlogServlet")
public class ChangeBlogServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("ChangeBlogServlet:doGet!");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");
		//获取参数
		Long userid = Long.valueOf(req.getParameter("userid"));
		int blogNo = Integer.parseInt(req.getParameter("blogno"));
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		int categoryIndex = Integer.parseInt(req.getParameter("category-index"));
		String mlabel = req.getParameter("mlabel");
		String slabel = req.getParameter("slabel");
		Date date = new Date();
		//不更新createdate
		Blog blog = new Blog(blogNo, userid, null, title, content, categoryIndex, mlabel,
				slabel, null, date);
		BlogService blogService = new BlogServiceImpl();
		Boolean bool = blogService.updateBlog(blog);
		Map<String, Object> info = new HashMap<>();
		info.put("change", bool);
		if (bool) {
			info.put("msg", "博客修改成功！");
		} else {
			info.put("msg", "博客修改失败！请重试");
		}
		//将map转为JSON，发送给客户端
		new ObjectMapper().writeValue(resp.getWriter(), info);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
