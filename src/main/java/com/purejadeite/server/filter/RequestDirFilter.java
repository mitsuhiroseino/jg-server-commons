package com.purejadeite.server.filter;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.purejadeite.server.util.RequestDir;

/**
 * リクエストの終了時にテンポラリディレクトリを削除するフィルターです。
 * @author mitsuhiroseino
 *
 */
public class RequestDirFilter implements Filter {

	private static Logger LOGGER = LoggerFactory.getLogger(RequestDirFilter.class);

	/**
	 * テンポラリフォルダの場所
	 */
	private String dir = "./storage/request";

	public RequestDirFilter() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
		// リクエスト完了時にディレクトリを削除
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		RequestDir.delete(session.getId());
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
		RequestDir.setRoot(root);
	}

	public void destroy() {
		RequestDir.delete();
	}
}