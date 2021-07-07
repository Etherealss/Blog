package controller.servlet.logged.manage;

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

/**
 * @author 寒洲
 * @description 删除博客
 * @date 2020/8/17
 */
@WebServlet("/DeleteBlogServlet")
public class DeleteBlogServlet extends HttpServlet {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.trace("删除博客");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//删除博客
		int blogNo = Integer.parseInt(req.getParameter("blogno"));
		BlogService blogService = new BlogServiceImpl();
		blogService.deleteBlog(blogNo);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
