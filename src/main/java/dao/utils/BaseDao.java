package dao.utils;

import dao.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 实现了数据库增删改查基本操作的抽象工具类，用于DAO实现类继承
 * @Author 寒洲
 * @Date 2020/7/24
 */
public abstract class BaseDao<T> {
	private Class<T> clazz = null;

	{
		//this指向继承BaseDAO的子类对象本身，获取该子类选择的泛型类型
		ParameterizedType paramType = (ParameterizedType) this.getClass().getGenericSuperclass();
		//获取泛型参数，赋值给clazz
		Type[] typeArguments = paramType.getActualTypeArguments();
		clazz = (Class<T>) typeArguments[0];

	}

	/**
	 * 通用的增、删、改操作
	 *
	 * @param conn 与数据库的连接
	 * @param sql  带占位符的sql语句
	 * @param args 可变长度参数用于填充占位符
	 * @return 返回int类型的参数显示影响了多少行数据
	 */
	public int update(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			// 填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			// 执行语句
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 资源关闭,不关闭连接
			JDBCUtils.closeResource(ps, null);

		}
		return 0;

	}

	/**
	 * 查询一行数据
	 *
	 * @param conn 与数据库的连接
	 * @param sql  带占位符的sql语句
	 * @param args 可变长度参数用于填充占位符
	 * @return 查询操作获取的对象
	 */
	public T getInstance(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			//填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			rs = ps.executeQuery();
			// 获取结果集的ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			// 获取结果集中的列数
			int columnCount = rsmd.getColumnCount();

			if (rs.next()) {
				T t = clazz.newInstance();
				// 处理结果集一行数据中的每一个列
				for (int i = 0; i < columnCount; i++) {
					// 获取列值
					Object columValue = rs.getObject(i + 1);

					// 获取每个列的列名
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// 给t对象指定获取的columnName属性，赋值为获取的columValue的数据
					Field field = clazz.getDeclaredField(columnLabel);
					//设置为可访问的
					field.setAccessible(true);
					field.set(t, columValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResource(ps, rs);
		}

		return null;
	}

	/**
	 * 查询多行数据，封装在对象中
	 *
	 * @param conn 与数据库的连接
	 * @param sql  带占位符的sql语句
	 * @param args 可变长度参数用于填充占位符
	 * @return 多行数据组成的List
	 */
	public List<T> getForObjectList(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			// 创建集合对象
			ArrayList<T> list = new ArrayList<T>();
			while (rs.next()) {
				//利用clazz指定返回的list的泛型类型
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) {
					// 获取列值
					Object columValue = rs.getObject(i + 1);
					// 获取每个列的列名
					String columnLabel = rsmd.getColumnLabel(i + 1);
					// 给t对象指定获取的columnName属性，赋值为获取的columValue的数据
					Field field = clazz.getDeclaredField(columnLabel);
					//设置为可访问的
					field.setAccessible(true);
					field.set(t, columValue);
				}
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResource(ps, rs);
		}
		return null;
	}

	/**
	 * 查询多行数据
	 *
	 * @param conn 与数据库的连接
	 * @param sql  带占位符的sql语句
	 * @param args 可变长度参数用于填充占位符
	 * @return 多行数据组成的List
	 */
	public List<String> getForStringList(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			// 创建集合对象
			ArrayList<String> list = new ArrayList<>();
			while (rs.next()) {
				//利用clazz指定返回的list的泛型类型
				// 获取列值
				String columValue = rs.getString(1);
				list.add(columValue);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResource(ps, rs);
		}
		return null;
	}


	/**
	 * 用于查询特殊值的通用的方法
	 *
	 * @param conn 与数据库的连接
	 * @param sql  带占位符的sql语句
	 * @param args 可变长度参数用于填充占位符
	 * @param <E>  查询得到的数据类型
	 * @return 查询得到的值
	 */
	public <E> E getValue(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				return (E) rs.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.closeResource(ps, rs);
		}
		return null;
	}
}
