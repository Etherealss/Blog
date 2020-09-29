package controller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 寒洲
 * @description 检查用户的登录状态
 * @date 2020/8/18
 */
@WebFilter("/*")
public class LoginFilter implements Filter {
	public void destroy() {}

	/**
	 * 需要拦截并判断登录状态的路径
	 */
	private static final Set<String> CHECK_PATHS = Collections.unmodifiableSet(
			new HashSet<>(Arrays.asList("/manage.html", "/write.html", "/ChangeBlogServlet",
					"/ChangeInfoServlet", "/ChangePwServlet", "/DeleteBlogServlet",
					"/DeleteUserServlet", "/UploadServlet", "/LogoutServlet", "/WriteServlet")));


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		//获取资源请求路径
		String uri = req.getRequestURI();
		//判断是否是登录相关的资源
		if (CHECK_PATHS.contains(uri)) {
			//获取登录状态
			Object user = req.getSession().getAttribute("userid");
			if (user != null) {
				//已登录，放行
				chain.doFilter(req, response);
			} else {
				//未登录
				System.out.println(uri + "   拦截");
				//前往这个提示页面 最后会跳转到首页
				resp.sendRedirect("/warning.html");
			}
		} else {
			//直接放行
			chain.doFilter(req, response);
		}


	}

	public void init(FilterConfig config) throws ServletException {}

}
