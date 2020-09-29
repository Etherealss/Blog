package dao.impl;

import bean.Blog;
import dao.BlogDao;
import dao.utils.BaseDao;

import java.sql.Connection;
import java.util.List;

/**
 * @author 寒洲
 * @description 对数据库博客数据的DAO操作
 * @date 2020/8/12
 */
public class BlogDaoImpl extends BaseDao<Blog> implements BlogDao {


	@Override
	public void insertNewBlog(Connection conn, Blog blog) {
		// 数据库第一个列是博客文章的序号，自动增长，不用输入
		// 第二列是作者的用户id
		String sql = "insert into `articles`" +
				"(author_id,title,content,createdate,updatedate,category,mlabel,slabel)" +
				"values(?,?,?,?,?,?,?,?)";
		update(conn, sql, blog.getAuthorId(), blog.getTitle(), blog.getContent(),
				blog.getCreatedate(), blog.getUpdatedate(), blog.getCategory(), blog.getMlabel(),
				blog.getSlabel());
	}

	@Override
	public int getBlogCount(Connection conn) {
		String countSql = "select count(*) from `articles`";
		//不能直接把Long转为int
		Number num = getValue(conn, countSql);
		return num.intValue();
	}

	@Override
	public int getBlogCountByCategory(Connection conn, int categoryIndex) {
		String countSql = "select count(*) from `articles` where category = ?";
		//不能直接把Long转为int
		Number num = getValue(conn, countSql, categoryIndex);
		return num.intValue();
	}

	@Override
	public int getBlogCountByMlabel(Connection conn, String mlabel) {
		String countSql = "select count(*) from `articles` where mlabel = ?";
		//不能直接把Long转为int
		Number num = getValue(conn, countSql, mlabel);
		return num.intValue();
	}

	@Override
	public int getBlogCountBySlabel(Connection conn, String slabel) {
		String countSql = "select count(*) from `articles` where slabel = ?";
		//不能直接把Long转为int
		Number num = getValue(conn, countSql, slabel);
		return num.intValue();
	}

	@Override
	public List<Blog> getBlogListByPage(Connection conn, int start, int rows) {
		String sql = "select no blogNo, author_id authorId,title,updatedate,category,mlabel,slabel" +
				" from `articles` limit ?, ?";
		return getForObjectList(conn, sql, start, rows);
	}

	@Override
	public List<Blog> getBlogListByCategory(Connection conn, int categoryIndex, int start, int rows) {
		String sql = "select no blogNo, author_id authorId,title,updatedate,category,mlabel,slabel" +
				" from `articles` where category = ? limit ?, ?";
		return getForObjectList(conn, sql, categoryIndex, start, rows);
	}

	@Override
	public List<Blog> getBlogListByMlabel(Connection conn, String mlabel, int start, int rows) {
		String sql = "select no blogNo, author_id authorId,title,updatedate,category,mlabel,slabel" +
				" from `articles` where mlabel = ? limit ?, ?";
		System.out.println("BlogDaoImpl mlabel = " + mlabel);
		return getForObjectList(conn, sql, mlabel, start, rows);
	}

	@Override
	public List<Blog> getBlogListBySlabel(Connection conn, String slabel, int start, int rows) {
		String sql = "select no blogNo, author_id authorId,title,updatedate,category,mlabel,slabel" +
				" from `articles` where slabel = ? limit ?, ?";
		return getForObjectList(conn, sql, slabel, start, rows);
	}


	@Override
	public Blog getBlogByNo(Connection conn, int no) {
		String sql = "select no blogNo,author_id authorId,title,content,createdate,updatedate," +
				"category,mlabel,slabel from `articles` where no = ?";
		return getInstance(conn, sql, no);
	}


	@Override
	public int getUserBlogCount(Connection conn, Long userid) {
		String countSql = "select count(*) from `articles` where author_id=?";
		//不能直接把Long转为int
		Number num = getValue(conn, countSql, userid);
		return num.intValue();
	}

	@Override
	public List<Blog> getUserBlogListByPage(Connection conn, Long userid, int start, int rows) {
		String sql = "select no blogNo,title,updatedate,category,mlabel,slabel " +
				"from `articles` where author_id=? limit ?, ? ";
		return getForObjectList(conn, sql, userid, start, rows);
	}

	@Override
	public void updateBlogByNo(Connection conn, Blog blog) {
		String infoSql = "update articles set title = ?,content = ?,category = ?,mlabel = ?," +
				"slabel = ?,updatedate = ? where no = ?";
		update(conn, infoSql, blog.getTitle(), blog.getContent(), blog.getCategory(), blog.getMlabel(),
				blog.getSlabel(), blog.getUpdatedate(), blog.getBlogNo());
	}

	@Override
	public void deleteBlogByNo(Connection conn, int blogNo) {
		String sql = "delete from articles where no = ?";
		update(conn, sql, blogNo);
	}

	@Override
	public List<Blog> getAllBlogMlabel(Connection conn) {
		String sql = "select no blogNo,mlabel from `articles`";
		return getForObjectList(conn, sql);
	}

	@Override
	public List<Blog> getAllBlogSlabel(Connection conn) {
		String sql = "select no blogNo,slabel from `articles`";
		return getForObjectList(conn, sql);
	}

	@Override
	public List<Blog> getAllBlogTitle(Connection conn) {
		String sql = "select no blogNo,title from `articles`";
		return getForObjectList(conn, sql);
	}

	@Override
	public List<Blog> getBlogNoByCategory(Connection conn, int category) {
		String sql = "select no blogNo from `articles` where category = ?";
		return getForObjectList(conn, sql, category);
	}

}
