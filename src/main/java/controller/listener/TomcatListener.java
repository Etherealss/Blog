package controller.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author 寒洲
 * @description Tomcat监听
 * @date 2020/8/21
 */

public class TomcatListener implements ServletContextListener {
	private final Logger logger = LoggerFactory.getLogger("simpleAsyncLogger");

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.trace("tomcat关闭......");
		//将数据库中用户在线状态设置为下线
		UserService us = new UserServiceImpl();
		us.logoutAllUser();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {}
}

