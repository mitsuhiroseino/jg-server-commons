package com.purejadeite.server.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.purejadeite.server.util.SessionDir;

/**
 * セッション毎にテンポラリディレクトリを削除するフィルターです。
 * @author mitsuhiroseino
 *
 */
public class SessionDirListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		// セッションのディレクトリを破棄
		HttpSession session = event.getSession();
		SessionDir.delete(session.getId());
	}
	
}