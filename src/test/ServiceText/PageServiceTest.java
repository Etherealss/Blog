package ServiceText;

import bean.Blog;
import bean.PageBean;
import org.junit.Test;
import service.PageService;
import service.impl.PageServiceImpl;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class PageServiceTest {

	PageService ps = new PageServiceImpl();

	@Test
	public void getBlogPageBean() {
		/*
		 * 按页码查询博客数据，并封装在PageBean中
		 * @param currentPage 当前页码
		 * @param rows 每页的记录数
		 * @return 包含了博客数据列表和分页信息的信息包
		 */
		PageBean<Blog> blogList = ps.getBlogPageBean(null,null,1, 5);
		System.out.println("博客数："+blogList.getTotalCount());
		for (Blog blog : blogList.getList()){
			System.out.println(blog.getBlogNo());
		}
	}

	@Test
	public void searchBlogPageBean() throws UnsupportedEncodingException {
		/*
		 * 按搜索词查询博客数据
		 * @param currentPage 当前页码
		 * @param rows 每页的记录数
		 * @param search 搜搜的条目
		 * @return 包含了博客数据列表和分页信息的信息包
		 */
		String[] searchExample = {"44444 1 可以 编程","测试","还是测试","可以互相跳转","隐秘的角落","可以即可(例: 头部、尾部)","编程"};
		String search = java.net.URLEncoder.encode(searchExample[0],"UTF-8");
		PageBean<Blog> blog = ps.searchBlogByWord(1, 10, search);
		System.out.println("最终获取到：" + blog.getList().size() +" 条博客");
		List<Blog> list = blog.getList();
		System.out.print("博客编号为： ");
		for (Blog b : list){
			System.out.print(b.getBlogNo()+ " ");
		}
		System.out.println("\nsearchBlogPageBean测试完毕");
	}

	@Test
	public void testContains() {
		/*
		 * 按页码查询博客数据，并封装在PageBean中
		 * @param currentPage 当前页码
		 * @param rows 每页的记录数
		 * @return 包含了博客数据列表和分页信息的信息包
		 */
		String[] s = {"44444 1 可以 测试","还是测试"};
		System.out.println("[ " + s[0] + " ]是否包含[ " + s[1] + " ] : " + s[0].contains(s[2]));
	}

	@Test
	public void getUserBlogPageBean() {
		/*
		 * 根据用户id查询其博客文章列表
		 * @param userid
		 * @param currentPage
		 * @param rows
		 * @return
		 */
		PageBean<Blog> userBlogPageBean = ps.getUserBlogPageBean(123123L, 1, 5);
		System.out.println("博客数: "+userBlogPageBean.getTotalCount());
		for(Blog blog : userBlogPageBean.getList()){
			System.out.println(blog.getBlogNo());
		}
	}
}