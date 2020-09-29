package service.impl;

import bean.Blog;
import dao.CategoryDao;
import dao.impl.CategoryDaoImpl;
import dao.BlogDao;
import dao.UserDao;
import dao.impl.BlogDaoImpl;
import dao.impl.UserDaoImpl;
import dao.utils.JDBCUtils;
import service.BlogService;

import java.sql.Connection;
import java.util.List;

/**
 * @author 寒洲
 * @description 获取和操作博客记录
 * @date 2020/8/15
 */
public class BlogServiceImpl implements BlogService {

	private final BlogDao blogDao = new BlogDaoImpl();

	@Override
	public List<String> getCategory() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			//修改博客
			CategoryDao cd = new CategoryDaoImpl();
			return cd.getBlogCategory(conn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeConnection(conn);
		}
		return null;
	}

	@Override
	public Blog getBlog(int blogNo) {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			//获取博客信息
			Blog blog = blogDao.getBlogByNo(conn, blogNo);
			//判断作者账号是否已注销（删除）
			if (blog.getAuthorId() != null) {
				//获取作者昵称
				UserDao userDao = new UserDaoImpl();
				String authorName = userDao.getUserNameById(conn, blog.getAuthorId());
				blog.setAuthorName(authorName);
			}
			return blog;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public Boolean insertBlog(Blog blog) {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			blogDao.insertNewBlog(conn, blog);
			System.out.println("WriteServlet：insert：成功");
			return true;
		} catch (Exception e) {
			System.out.println("WriteServlet：insert：error");
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public Boolean updateBlog(Blog blog) {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			//修改博客
			blogDao.updateBlogByNo(conn, blog);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public void deleteBlog(int blogNo) {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			//删除博客
			blogDao.deleteBlogByNo(conn, blogNo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeConnection(conn);
		}
	}
}
