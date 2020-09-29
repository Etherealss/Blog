package controller.listener;

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

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("tomcat关闭......");
		//将数据库中用户在线状态设置为下线
		UserService us = new UserServiceImpl();
		us.logoutAllUser();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {}
}

