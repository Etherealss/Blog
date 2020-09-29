package service.impl;

import bean.User;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import dao.utils.JDBCUtils;
import service.UserService;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 寒洲
 * @description
 * @date 2020/8/17
 */
public class UserServiceImpl implements UserService {

	private final UserDao userDao = new UserDaoImpl();

	@Override
	public Boolean register(User user) {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			userDao.insertNewUser(conn, user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public Map<String, Object> login(Long id, String password){
		Connection conn = null;
		//返回信息的map
		Map<String, Object> info = new HashMap<>();
		try {
			conn = JDBCUtils.getConnection();
			//封装user对象
			if(userDao.existUserById(conn,id)){
				//存在该用户
				//判断是否已登录
				if (userDao.validateState(conn,id)){
					info.put("login", false);
					info.put("msg", "注意！该账户已经登录！");
				}else if(userDao.validatePw(conn,id,password)){
					//未登录，判断用户密码
					//密码正确，登录成功
					info.put("login", true);
					info.put("msg", "恭喜这位小可爱登录成功！");
					//更新数据库的在线状态为：在线true
					userDao.updateState(conn, true, id);
					//根据id查询用户数据
					User user = userDao.getUserById(conn, id);
					info.put("user", user);
				}else{
					//密码错误，登录失败
					info.put("login", false);
					info.put("msg", "用户密码错误！");
				}
			}else{
				//用户不存在，登录失败
				info.put("login", false);
				info.put("msg", "用户不存在！请仔细瞅瞅");
			}
		} catch (Exception e) {
			e.printStackTrace();
			info.put("login", false);
			info.put("msg", "登录失败!！请重试！");
		}finally {
			JDBCUtils.closeConnection(conn);
		}
		return info;
	}

	@Override
	public Boolean changeAvatar(Long userid, byte[] byteImg) {
		Connection conn = null;
		try{
			conn = JDBCUtils.getConnection();
			userDao.updateAvatar(conn, userid, byteImg);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public Boolean changeInfo(User user, Long originalId) {
		Connection conn = null;
		try{
			conn = JDBCUtils.getConnection();
			userDao.updateInfo(conn, user, originalId);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public Boolean changePw(Long userid, String originalPw, String newPw) {
		Connection conn = null;
		try{
			conn = JDBCUtils.getConnection();
			//首先判断原密码是否正确
			if (userDao.validatePw(conn, userid, originalPw)) {
				//密码正确返回true，并修改密码
				userDao.updatePw(conn, userid, originalPw, newPw);
				return true;
			}
			//密码错误返回false，不修改密码
			return false;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public Boolean deleteUser(User user) {
		Connection conn = null;
		Long userid = user.getId();
		String password = user.getPassword();
		try {
			conn = JDBCUtils.getConnection();
			//查询用户账号密码是否正确
			if (userDao.validatePw(conn, userid, password)) {
				//账号密码正确，删除数据
				userDao.deleteById(conn, userid);
				return true;
			} else {
				//账号密码错误，查询用户数据
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public User getUserInfo(Long userid) {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			//查询账号个人信息并返回
			return userDao.getUserById(conn,userid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public byte[] getUserAvatar(Long id) {
		Connection conn = null;
		try{
			conn = JDBCUtils.getConnection();
			return userDao.getUserAvatarById(conn, id);
		} catch (Exception e) {
			e.printStackTrace();
			//如果遇到异常就返回null，表示没有获取到数据
			return null;
		}finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public Boolean validateUserId(Long userid) {
		Connection conn = null;
		try{
			conn = JDBCUtils.getConnection();
			//查询用户id，不存在为false
			return userDao.existUserById(conn, userid);
		}catch (Exception e){
			e.printStackTrace();
			//出现异常返回null
			return null;
			/*
			按道理说，Boolean非黑即白，返回null好像说不过去
			不过我觉得可以解释为：出现了异常所以用户ID是否存在并不清楚
			也就是说null蕴含着：不清楚是true还是false的意思
			正好可以说明出现异常的处境
			正常情况下Boolean肯定只有true和false啦
			这里感觉可能会有“把null当成第三个布尔值来判断”的嫌疑
			所以狡辩一下o(￣ˇ￣)o
			 */
		}finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public void logout(Long userid) {
		Connection conn = null;
		try{
			conn = JDBCUtils.getConnection();
			//将在线状态修改为：下线false
			userDao.updateState(conn, false, userid);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			JDBCUtils.closeConnection(conn);
		}
	}

	@Override
	public void logoutAllUser() {
		Connection conn = null;
		try{
			conn = JDBCUtils.getConnection();
			//将在线状态修改为：下线false
			userDao.logoutAllUser(conn);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			JDBCUtils.closeConnection(conn);
		}
	}
}
