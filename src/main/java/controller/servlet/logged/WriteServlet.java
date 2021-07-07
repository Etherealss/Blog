package controller.servlet.logged;

import com.fasterxml.jackson.databind.ObjectMapper;
import bean.Blog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @description 上传新发布的博客文章
 * @date 2020/8/12
 */
@WebServlet("/WriteServlet")
public class WriteServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("发布文章");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");

		//获取参数
		Long userid = Long.valueOf(req.getParameter("userid"));
		String title = req.getParameter("title");
		logger.info("发布博客：用户id：{}，博客标题：{}", userid, title);

		String content = req.getParameter("content");
		int categoryIndex = Integer.parseInt(req.getParameter("category-index"));
		String mlabel = req.getParameter("mlabel");
		String slabel = req.getParameter("slabel");
		Date date = new Date();

		Blog blog = new Blog(userid, title, content, categoryIndex, mlabel, slabel, date, date);

		Map<String, Object> info = new HashMap<>();
		//调用下面的方法进行数据库的操作，返回结果
		BlogService bs = new BlogServiceImpl();
		info.put("write", bs.insertBlog(blog));
		//将map转为JSON
		ObjectMapper mapper = new ObjectMapper();
		//发送给客户端
		mapper.writeValue(resp.getWriter(), info);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
