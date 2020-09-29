package service;

import bean.Blog;

import java.util.Arrays;
import java.util.List;

/**
 * @author 寒洲
 * @description 规定操作博客记录的接口
 * @date 2020/8/15
 */
public interface BlogService {

	/**
	 * 获取博客分类
	 * @return 博客分类数组
	 */
	List<String> getCategory();

	/**
	 * 获取博客的详细数据
	 * @param blogNo 博客序号
	 * @return 博客数据包
	 */
	Blog getBlog(int blogNo);

	/**
	 * 发布博客
	 * @param blog
	 * @return
	 */
	Boolean insertBlog(Blog blog);

	/**
	 * 修改博客
	 * @param blog
	 * @return 出现意外返回false
	 */
	Boolean updateBlog(Blog blog);

	/**
	 * 删除博客
	 * @param blogNo
	 */
	void deleteBlog(int blogNo);
}
