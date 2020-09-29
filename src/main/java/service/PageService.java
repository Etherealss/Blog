package service;

import bean.Blog;
import bean.PageBean;

import java.sql.Connection;

/**
 * @author 寒洲
 * @description 规范获取页面信息的接口
 * @date 2020/8/15
 */
public interface PageService {

	/**
	 * 按页码查询博客数据，并封装在PageBean中
	 * @param type 查找方式（查询所有、按分类/主要标签/次要标签查询）
	 * @param msg 查找的内容
	 * @param currentPage 当前页码
	 * @param rows 每页的记录数
	 * @return 包含了博客数据列表和分页信息的信息包
	 */
	PageBean<Blog> getBlogPageBean(String type, String msg, int currentPage, int rows);

	/**
	 * 按页码查询博客数据，并封装在PageBean中
	 * @param currentPage 当前页码
	 * @param rows 每页的记录数
	 * @param word 搜搜的条目
	 * @return 包含了博客数据列表和分页信息的信息包
	 */
	PageBean<Blog> searchBlogByWord(int currentPage, int rows, String word);

	/**
	 * 根据用户id查询其博客文章列表
	 * @param userid
	 * @param currentPage
	 * @param rows
	 * @return
	 */
	PageBean<Blog> getUserBlogPageBean(Long userid, int currentPage, int rows);
}
