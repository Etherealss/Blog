package dao;

import bean.Blog;

import java.sql.Connection;
import java.util.List;

/**
 * @author 寒洲
 * @description 规范操作数据库博客表的接口
 * @date 2020/8/12
 */
public interface BlogDao {


	/**
	 * 通过连接向数据库插入数据
	 * @param conn 数据库连接
	 * @param blog 博客信息类
	 */
	void insertNewBlog(Connection conn, Blog blog);

	/**
	 * 获取博客总数
	 * @param conn 数据库连接
	 * @return 总博客数目
	 */
	int getBlogCount(Connection conn);

	/**
	 * 获取对应博客分类的博客总数
	 * @param conn
	 * @param categoryIndex
	 * @return
	 */
	int getBlogCountByCategory(Connection conn, int categoryIndex);

	/**
	 * 获取对应主要标签的博客总数
	 * @param conn
	 * @param mlabel
	 * @return
	 */
	int getBlogCountByMlabel(Connection conn, String mlabel);

	/**
	 * 获取对应次要标签的博客总数
	 * @param conn
	 * @param slabel
	 * @return
	 */
	int getBlogCountBySlabel(Connection conn, String slabel);

	/**
	 * 按页码查询每页的博客记录，不包含博客内容和创建日期（博客列表不显示）
	 * @param conn 数据库连接
	 * @param start 博客记录的起始索引
	 * @param rows 每一页显示的记录行数，也就是每一次查询要获取的记录数
	 * @return 包含了博客数据的List<Blog>
	 */
	List<Blog> getBlogListByPage(Connection conn, int start, int rows);

	/**
	 * 按<strong>博客分类</strong>查询每页的博客记录
	 * @param conn 数据库连接
	 * @param categoryIndex 博客分类索引
	 * @param start 博客记录的起始索引
	 * @param rows 每一页显示的记录行数，也就是每一次查询要获取的记录数
	 * @return 包含了博客数据的List<Blog>
	 */
	List<Blog> getBlogListByCategory(Connection conn, int categoryIndex, int start, int rows);

	/**
	 * 按<strong>主要标签</strong>查询每页的博客记录，不包含博客内容和创建日期
	 * @param conn 数据库连接
	 * @param malebl 主要标签
	 * @param start 博客记录的起始索引
	 * @param rows 每一页显示的记录行数，也就是每一次查询要获取的记录数
	 * @return 包含了博客数据的List<Blog>
	 */
	List<Blog> getBlogListByMlabel(Connection conn, String malebl, int start, int rows);

	/**
	 * 按<strong>次要标签</strong>查询每页的博客记录，不包含博客内容和创建日期
	 * @param conn 数据库连接
	 * @param salebl 次要要标签
	 * @param start 博客记录的起始索引
	 * @param rows 每一页显示的记录行数，也就是每一次查询要获取的记录数
	 * @return 包含了博客数据的List<Blog>
	 */
	List<Blog> getBlogListBySlabel(Connection conn, String salebl, int start, int rows);

	/**
	 * 按博客序号查询博客内容
	 * @param conn 数据库连接
	 * @param no 博客序号
	 * @return 封装了博客信息的Blog对象
	 */
	Blog getBlogByNo(Connection conn, int no);

	/**
	 * 获取指定用户的博客总数
	 * @param conn 数据库
	 * @param userid 用户id
	 * @return 博客数
	 */
	int getUserBlogCount(Connection conn, Long userid);

	/**
	 * 按页码查询用户每页要显示的博客记录
	 * @param conn 数据库连接
	 * @param userid 用户id
	 * @param start 博客记录的起始索引
	 * @param rows 每一页显示的记录行数，也就是每一次查询要获取的记录数
	 * @return 包含了用户的博客的数据
	 */
	List<Blog> getUserBlogListByPage(Connection conn, Long userid, int start, int rows);

	/**
	 * 修改博客
	 * @param conn 数据库连接
	 * @param blog 博客信息包
	 */
	void updateBlogByNo(Connection conn, Blog blog);

	/**
	 * 删除博客
	 * @param conn 数据库连接
	 * @param blogNo 博客编号
	 */
	void deleteBlogByNo(Connection conn, int blogNo);

	/**
	 * 获取所有博客的主要标签
	 * @param conn 数据库连接
	 * @return
	 */
	List<Blog> getAllBlogMlabel(Connection conn);
	/**
	 * 获取所有博客的次要标签
	 * @param conn 数据库连接
	 * @return
	 */
	List<Blog> getAllBlogSlabel(Connection conn);
	/**
	 * 获取所有博客的标题
	 * @param conn 数据库连接
	 * @return
	 */
	List<Blog> getAllBlogTitle(Connection conn);

	/**
	 * 获取某一分类下的所有博客
	 * @param conn
	 * @param category
	 * @return
	 */
	List<Blog> getBlogNoByCategory(Connection conn, int category);
}
