package DaoText;

import bean.Blog;
import dao.BlogDao;
import dao.impl.BlogDaoImpl;
import dao.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class BlogDaoTest {
	private final BlogDao dao = new BlogDaoImpl();
	private Connection conn;

	{
		try {
			conn = JDBCUtils.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出博客内容
	 * @param list
	 */
	private void showList(List<Blog> list){
		for(Blog blog : list){
			System.out.println(blog);
		}
	}

	@Test
	public void insertNewBlog() {
	}

	@Test
	public void getBlogCount() {
		System.out.println(dao.getBlogCount(conn));
	}

	@Test
	public void getBlogListByPage() {
	}

	@Test
	public void getBlogByNo() {
	}

	@Test
	public void getUserBlogCount() {
	}

	@Test
	public void getUserBlogListByPage() {
	}

	@Test
	public void updateBlogByNo() {
	}

	@Test
	public void deleteBlogByNo() {
	}


	@Test
	public void getAllBlogMlabel() {
		List<Blog> allBlogMainLabel = dao.getAllBlogMlabel(conn);
		for(Blog blog : allBlogMainLabel){
			System.out.println(blog);
		}
	}

	@Test
	public void getAllBlogSlabel() {
		List<Blog> allBlogMainLabel = dao.getAllBlogSlabel(conn);
		for(Blog blog : allBlogMainLabel){
			System.out.println(blog);
		}
	}

	@Test
	public void getAllBlogTitle() {
		List<Blog> allBlogMainLabel = dao.getAllBlogTitle(conn);
		for(Blog blog : allBlogMainLabel){
			System.out.println(blog);
		}
	}


	@Test
	public void getBlogListByMlabel() {
		showList(dao.getBlogListByMlabel(conn,"前端",0,5));
		System.out.println("getBlogListByMlabel测试完毕");
	}

	@Test
	public void getBlogListBySlabel() {
		showList(dao.getBlogListBySlabel(conn,"1",0,5));
		System.out.println("getBlogListBySlabel测试完毕");
	}

	@Test
	public void getBlogListByCategory() {
		showList(dao.getBlogListByCategory(conn,1,0,5));
		System.out.println("getBlogListByCategory测试完毕");
	}

	@Test
	public void getBlogNoByCategory() {
		showList(dao.getBlogNoByCategory(conn,1));
		System.out.println("ps:数据库category从1开始，此处不获取Blog的category属性，0是jdbc装int时的null值");
		System.out.println("getBlogNoByCategory测试完毕");

	}

	@Test
	public void getBlogCountByCategory() {
	}

	@Test
	public void getBlogCountByMlabel() {
	}

	@Test
	public void getBlogCountBySlabel() {
	}

}