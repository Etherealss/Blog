package utils;

import bean.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 寒洲
 * @description 有关Cookie的通用操作的工具类
 * @date 2020/8/8
 */
public abstract class CookieUtils {

	private final static Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	/**
	 * 将用户信息保存在cookie中
	 * 用用户id作为Cookie名，其他信息用"&"拼成字符串作为value
	 * 信息包括：昵称、生日、性别
	 * @param user
	 */
	public static Cookie setUserCookie(User user){
		//Cookie保存数据
		//用用户id作为Cookie名
		String id = user.getId().toString();
		String nickname = user.getNickname();
		Date birthday = user.getBirthday();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String birthdayString = formatter.format(birthday);
		Boolean sex = user.getSex();
		String info = "nickname=" + nickname + "&birthday=" + birthdayString + "&sex=" + sex;
		logger.trace("CookieUtils:setCookie: info = " + info);
		Cookie cookie=new Cookie(id, info);
		//设置Cookie生效的时间：1小时
		cookie.setMaxAge(3600);
		return cookie;
	}
}
