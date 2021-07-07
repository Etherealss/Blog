package dao.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author 寒洲
 * @description 数据库工具类
 * @date 2020/7/24
 */
public class JdbcUtils {
	/**
	 * 数据库DataSource
	 */
	static DataSource source = null;

	static {
		InputStream is = null;
		try {
			Properties pros = new Properties();
			is = JdbcUtils.class.getClassLoader().getResourceAsStream("druid.properties");
			pros.load(is);
			source = DruidDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			doClose(is);
		}



	}

	/**
	 * 获取数据库连接
	 * @return Connection 数据库的连接
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return source.getConnection();
	}

	/**
	 * 关闭数据库资源
	 * @param conn 数据库连接
	 * @param ps   Statement对象
	 * @param rs   ResultSet对象
	 */
	public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
		doClose(conn);
		doClose(ps);
		doClose(rs);
	}

	/**
	 * 关闭数据库资源，不关闭数据库连接
	 * @param ps Statement对象
	 * @param rs ResultSet对象
	 */
	public static void closeResource(Statement ps, ResultSet rs) {
		doClose(ps);
		doClose(rs);
	}

	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		doClose(conn);
	}

	/**
	 * 关闭可关闭的资源
	 * @param closeable
	 */
	private static void doClose(AutoCloseable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


}
