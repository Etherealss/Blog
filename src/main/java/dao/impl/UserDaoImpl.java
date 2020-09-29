package dao.impl;

import bean.User;
import dao.UserDao;
import dao.utils.BaseDao;

import java.sql.Connection;
import java.util.List;

/**
 * @Description 对数据库用户表的DAO操作
 * @Author 寒洲
 * @Date 2020/7/24
 */
public class UserDaoImpl extends BaseDao<User> implements UserDao {

	@Override
	public void insertNewUser(Connection conn, User user) {
		//数据库中，user_password表的id列是user_info的外键
		//插入用户ID和密码
		String pwSql = "insert into `user_password`(id,password)values(?,?)";
		update(conn, pwSql, user.getId(), user.getPassword());
		//插入用户信息
		String infoSql = "insert into `user_info`(id,nickname,sex,avatar,birthday)values(?,?,?,?,?)";
		update(conn, infoSql, user.getId(), user.getNickname(), user.getSex(), user.getAvatar(), user.getBirthday());
	}

	@Override
	public void deleteById(Connection conn, Long id) {
		String sql = "delete from user_password where id = ?";
		update(conn, sql, id);
		//由于外键，user_info中的数据也会删除
		//用户的博客不会删除
	}

	@Override
	public Boolean validateState(Connection conn, Long id) {
		String stateSql = "select state from `user_password` where id = ?";
		return getValue(conn, stateSql, id);
	}

	@Override
	public void updateState(Connection conn, Boolean state, Long id) {
		String pwSql = "update user_password set state = ? where id = ?";
		update(conn, pwSql, state, id);
	}

	@Override
	public Boolean validatePw(Connection conn, Long id, String password) {
		String existSql = "select count(*) from `user_password` where id = ? and password = ?";
		return getValue(conn, existSql, id, password).equals(1L);
	}


	@Override
	public void updateInfo(Connection conn, User user, Long originalId) {
		//先修改存在外键依赖的表id
		String pwSql = "update user_password set id = ? where id = ?";
		update(conn, pwSql, user.getId(), originalId);
		String infoSql = "update user_info set id = ?,nickname = ?,sex = ?,birthday = ? where id = ?";
		update(conn, infoSql, user.getId(), user.getNickname(), user.getSex(), user.getBirthday(), originalId);
	}

	@Override
	public void updatePw(Connection conn, Long id, String originalPw, String newPw) {
		String sql = "update user_password set password = ? where id = ?";
		update(conn, sql, newPw, id);
		System.out.println("UserDaoImpl:updatePw成功！");
	}

	@Override
	public void updateAvatar(Connection conn, Long id, byte[] avatar) {
		String sql = "update user_info set avatar = ? where id = ?";
		update(conn, sql, avatar, id);
	}

	@Override
	public Boolean existUserById(Connection conn, Long id) {
		String sql = "select count(*) from `user_password` where id = ?";
		//mysql数据库中统计函数count()的数据类型是DECIMAL对应java对应的数据类型是Long
		//equals语句判断得到的结果是否为1L，若是则返回true，说明存在该用户
		return getValue(conn, sql, id).equals(1L);
	}

	@Override
	public User getUserById(Connection conn, Long id) {
		String sql = "select id,nickname,sex,birthday from `user_info` where id = ?";
		return getInstance(conn, sql, id);
	}

	@Override
	public String getUserNameById(Connection conn, Long id) {
		String sql = "select nickname from `user_info` where id = ?";
		return getValue(conn, sql, id);
	}

	@Override
	public byte[] getUserAvatarById(Connection conn, Long id) {
		String sql = "select avatar from `user_info` where id = ?";
		return getValue(conn, sql, id);
	}

	@Override
	public List<User> getAllUser(Connection conn) {
		String sql = "select id,nickname,sex,avatar,birthday from `user_info`";
		return getForObjectList(conn, sql);
	}

	@Override
	public void logoutAllUser(Connection conn) {
		String sql = "update user_password set state = ?";
		update(conn, sql, false);
	}
}
