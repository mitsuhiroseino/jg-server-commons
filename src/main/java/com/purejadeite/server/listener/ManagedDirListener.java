package com.purejadeite.server.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.purejadeite.server.util.ManagedDir;

/**
 * アプリケーションの終了時にテンポラリディレクトリを削除するリスナーです。
 * @author mitsuhiroseino
 *
 */
public class ManagedDirListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// 管理しているディレクトリを全て破棄
		ManagedDir.destroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
	}
	
}