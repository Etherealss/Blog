package controller.servlet.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import bean.Blog;
import bean.PageBean;
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
 * @description 获取分页和页面的博客数据
 * @date 2020/8/13
 */
@WebServlet("/GetPageServlet")
public class GetPageServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("GetPageServlet:doGet!");
		//设置编码
		req.setCharacterEncoding("utf-8");
		//设置响应的数据格式为json
		resp.setContentType("application/json;charset=utf-8");

		//获取参数
		//当前页码
		int cerrentPage = Integer.parseInt(req.getParameter("current-page"));
		//每页显示的数据条数
		int rows = Integer.parseInt(req.getParameter("rows"));
		//搜索词
		String search = req.getParameter("search");
		String category = req.getParameter("category");
		String mlabel = req.getParameter("mlabel");
		String slabel = req.getParameter("slabel");


		//获取分页信息和博客数据
		PageService ps = new PageServiceImpl();
		//返回的分页数据
		PageBean<Blog> page = null;
		if (search != null) {
			//按搜索词查询博客
			page = ps.searchBlogByWord(cerrentPage, rows, search);
		} else if(category != null){
			//按博客分类查询博客
			page = ps.getBlogPageBean("category",category,cerrentPage, rows);
		}else if(mlabel != null){
			//按主要标签查询博客
			page = ps.getBlogPageBean("mlabel",mlabel,cerrentPage, rows);
		}else if(slabel != null){
			//按次要标签查询博客
			page = ps.getBlogPageBean("slabel",slabel,cerrentPage, rows);
		}else {
			//啥都没有，显示所有
			page = ps.getBlogPageBean(null,null,cerrentPage, rows);
		}
		//返回信息的map
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("page", page);
		//将map转为JSON
		ObjectMapper mapper = new ObjectMapper();
		//发送给客户端
		mapper.writeValue(resp.getWriter(), info);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
