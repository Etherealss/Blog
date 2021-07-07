package controller.servlet.user;

import bean.Blog;
import bean.PageBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.PageService;
import service.impl.PageServiceImpl;

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
 * @description 获取用户博客列表
 * @date 2020/8/16
 */
@WebServlet("/GetUserBlogListServlet")
public class GetUserBlogListServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("获取用户的博客列表");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");

		//获取参数
		Long userid = Long.valueOf(req.getParameter("userid"));
		int cerrentPage = Integer.parseInt(req.getParameter("current-page"));
		int rows = Integer.parseInt(req.getParameter("rows"));
		//获取用户个人中心当前页的页面信息
		PageService pageService = new PageServiceImpl();
		PageBean<Blog> userBlogPageBean = pageService.getUserBlogPageBean(userid, cerrentPage, rows);
		//返回信息的map
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("page", userBlogPageBean);
		//将map转为JSON
		ObjectMapper mapper = new ObjectMapper();
		//发送给客户端
		mapper.writeValue(resp.getWriter(), info);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
