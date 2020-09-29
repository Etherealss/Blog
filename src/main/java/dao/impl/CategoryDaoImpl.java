package dao.impl;

import dao.CategoryDao;
import dao.utils.BaseDao;

import java.sql.Connection;
import java.util.List;

/**
 * @author 寒洲
 * @description
 * @date 2020/8/19
 */
public class CategoryDaoImpl extends BaseDao<String> implements CategoryDao {
	@Override
	public List<String> getBlogCategory(Connection conn) {
		String sql = "select name from `category`";
		return getForStringList(conn, sql);
	}
}
