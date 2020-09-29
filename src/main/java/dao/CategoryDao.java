package dao;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 * @author 寒洲
 * @description
 * @date 2020/8/19
 */
public interface CategoryDao {


	/**
	 * 获取博客分类
	 * @param conn
	 * @return 博客分类数组
	 */
	List<String> getBlogCategory(Connection conn);
}
