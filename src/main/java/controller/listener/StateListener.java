package controller.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author 寒洲
 * @description 判断用户在线状况的监听器1
 * @date 2020/8/21
 */
public class StateListener implements HttpSessionListener {

	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.trace("创建session");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		//用户session连接断开，异常退出
		HttpSession session = se.getSession();
		Object userid = session.getAttribute("userid");
		logger.trace("StateListener 用户session连接断开 / 用户退出 : " + userid);
		if (userid != null) {
			UserService us = new UserServiceImpl();
			us.logout(Long.valueOf(userid.toString()));
		}
	}
}
