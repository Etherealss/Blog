package controller.servlet.blog;

import bean.Blog;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author 寒洲
 * @description 获取博客的详细数据
 * @date 2020/8/15
 */
@WebServlet("/GetBlogServlet")
public class GetBlogServlet extends HttpServlet {
	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("获取博客的详细数据");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");

		//获取参数
		int blogNo = Integer.parseInt(req.getParameter("blogno"));
		//通过参数获取博客数据
		BlogService blogService = new BlogServiceImpl();
		Blog blog = blogService.getBlog(blogNo);

		//返回信息的map
		Map<String, Object> info = new HashMap<String, Object>();
		if (blog != null){
			//获取到数据
			info.put("blog",blog);
			info.put("get",true);
		}else{
			//出现意外状况
			info.put("get", false);
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
