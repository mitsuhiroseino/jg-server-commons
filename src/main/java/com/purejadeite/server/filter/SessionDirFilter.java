package com.purejadeite.server.filter;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.purejadeite.server.util.SessionDir;

/**
 * セッション毎のテンポラリフォルダを管理するフィルターです。
 * @author mitsuhiroseino
 *
 */
public class SessionDirFilter implements Filter {

	private static Logger LOGGER = LoggerFactory.getLogger(SessionDirFilter.class);

	/**
	 * テンポラリフォルダの場所
	 */
	private String dir = "./storage/session";

	public SessionDirFilter() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) {
		String dir = filterConfig.getInitParameter("dir");
		if (dir != null) {
			this.dir = dir;
		}
		File root = new File(this.dir);
		root.mkdirs();
		try {
			FileUtils.cleanDirectory(root);
		} catch (IOException e) {
			LOGGER.warn(root.getPath() + "を初期化出来ませんでした。");
		}
		SessionDir.setRoot(root);
	}

	public void destroy() {
		SessionDir.delete();
	}
}