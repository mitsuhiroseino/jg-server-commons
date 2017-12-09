package com.purejadeite.server.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * リソースファイルを返すサーブレットです。
 *
 * @author mitsuhiroseino
 *
 */
public class ResourceServlet extends HttpServlet {

	private static final long serialVersionUID = -3078013890473416358L;

	/**
	 * ロガー
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServlet.class);

	private static final String INIT_DEFAULT_PATH = "defaultPath";
	private static final String INIT_MAPPING = "mapping";

	private File root;
	private Map<String, String> pathMapping;

	public ResourceServlet() {
	}

	public void init() {
		// 初期化コンフィグを取得
		ServletConfig config = getServletConfig();

		String defaultPath = config.getInitParameter(INIT_DEFAULT_PATH);
		if (!StringUtils.isEmpty(defaultPath)) {
			this.root = new File(defaultPath);
		}
		
	}

	/**
	 * GET時の処理
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		ServletContext context = request.getServletContext();
		String servletPath = request.getServletPath();
	}

}