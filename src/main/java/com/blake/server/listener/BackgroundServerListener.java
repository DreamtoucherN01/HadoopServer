package com.blake.server.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BackgroundServerListener implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {

		System.out.print("stop");
	}

	public void contextInitialized(ServletContextEvent arg0) {

		System.out.print("start");
	}

}
