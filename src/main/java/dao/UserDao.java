package dao;

import bean.User;

import java.sql.Connection;
import java.util.List;

/**
 * @Description 规范操作数据库两个用户表的接口
 * @Author 寒洲
 * @Date 2020/7/24
 */
public interface UserDao {
	/**
	 * 通过连接向数据库插入数据
	 *
	 * @param conn 数据库连接
	 * @param user 用户信息类
	 */
	void insertNewUser(Connection conn, User user);

	/**
	 * 通过id删除数据库中指定的用户信息
	 *
	 * @param conn 数据库连接
	 * @param id   删除的用户的id
	 */
	void deleteById(Connection conn, Long id);

	/**
	 * 判断用户是否已登录
	 * @param conn     数据库连接
	 * @param id       用户id
	 * @return Boolean 正确返回true
	 */
	Boolean validateState(Connection conn, Long id);

	/**
	 * 更新用户的在线状态
	 * @param conn     数据库连接
	 * @param id       用户id
	 */
	void updateState(Connection conn, Boolean state, Long id);

	/**
	 * 判断用户密码是否正确
	 *
	 * @param conn     数据库连接
	 * @param id       用户id
	 * @param password 用户密码
	 * @return Boolean 正确返回true
	 */
	Boolean validatePw(Connection conn, Long id, String password);

	/**
	 * 根据用户原始ID修改数据库中指定的用户信息
	 *
	 * @param conn       数据库连接
	 * @param user       用户信息类
	 * @param originalId 用户原ID
	 */
	void updateInfo(Connection conn, User user, Long originalId);

	/**
	 * 根据用户id修改密码
	 *
	 * @param conn       数据库连接
	 * @param id         用户id
	 * @param originalPw 原密码
	 * @param newPw      新密码
	 */
	void updatePw(Connection conn, Long id, String originalPw, String newPw);

	/**
	 * 修改用户头像，将新头像存在数据库
	 *
	 * @param conn   数据库连接
	 * @param id     用户id
	 * @param avatar 头像的数据
	 */
	void updateAvatar(Connection conn, Long id, byte[] avatar);

	/**
	 * 判断用户是否存在
	 *
	 * @param conn 数据库连接
	 * @param id   用户ID
	 * @return 用户存在则返回true
	 */
	Boolean existUserById(Connection conn, Long id);

	/**
	 * 通过账号和密码获取用户详细数据。
	 * @param conn     数据库连接
	 * @param id       用户ID
	 * @return 具有详细数据的User对象
	 */
	User getUserById(Connection conn, Long id);

	/**
	 * 通过用户ID获取用户昵称，用于显示在博客上
	 *
	 * @param conn 数据库连接
	 * @param id   用户ID
	 * @return 用户昵称字符串
	 */
	String getUserNameById(Connection conn, Long id);

	/**
	 * 通过id从数据库查询、获取用户头像数据
	 *
	 * @param conn 数据库连接
	 * @param id   用户ID
	 * @return 用户头像的byte[]数据
	 */
	byte[] getUserAvatarById(Connection conn, Long id);

	/**
	 * 获取所有已注册用户信息
	 *
	 * @param conn 数据库连接
	 * @return 所有用户的List
	 */
	List<User> getAllUser(Connection conn);

	/**
	 * 更新所有用户在数据库的在线状态为下线
	 * @param conn
	 */
	void logoutAllUser(Connection conn);
}
