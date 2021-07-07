package DaoText;

import dao.CategoryDao;
import dao.impl.CategoryDaoImpl;
import dao.utils.JdbcUtils;
import org.junit.Test;


import java.sql.Connection;
import java.util.List;

public class CategoryDaoImplTest {
	private CategoryDao categoryDao = new CategoryDaoImpl();
	private Connection conn;

	{
		try {
			conn = JdbcUtils.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void getBlogCategory() {
		List<String> blogCategory = categoryDao.getBlogCategory(conn);
		System.out.println(blogCategory);
	}
}