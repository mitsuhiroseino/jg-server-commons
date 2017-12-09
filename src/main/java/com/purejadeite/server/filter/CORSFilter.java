package com.purejadeite.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * クロス・オリジン・リソース・シェアリング(CORS)を可能にするフィルターです。
 * @author mitsuhiroseino
 *
 */
public class CORSFilter implements Filter {

	/**
	 * アクセスを許可するURI
	 */
	private String allowOrigin = "*";

	/**
	 * アクセスを許可するメソッド
	 */
	private String allowMethods = "POST, GET, PUT, DELETE, OPTIONS";

	/**
	 * プリフライトの結果をキャッシュしてよい時間
	 */
	private String maxAge = "3600";

	/**
	 *
	 */
	private String allowHeaders = "Origin, X-Requested-With, Content-Type, Accept";

	public CORSFilter() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Access-Control-Allow-Origin", this.allowOrigin);
		httpResponse.setHeader("Access-Control-Allow-Methods",
				this.allowMethods);
		httpResponse.setHeader("Access-Control-Max-Age", this.maxAge);
		httpResponse.setHeader("Access-Control-Allow-Headers",
				this.allowHeaders);
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) {
		String allowOrigin = filterConfig.getInitParameter("allowOrigin");
		if (allowOrigin != null) {
			this.allowOrigin = allowOrigin;
		}
		String allowMethods = filterConfig.getInitParameter("allowMethods");
		if (allowMethods != null) {
			this.allowMethods = allowMethods;
		}
		String maxAge = filterConfig.getInitParameter("maxAge");
		if (maxAge != null) {
			this.maxAge = maxAge;
		}
		String allowHeaders = filterConfig.getInitParameter("allowHeaders");
		if (allowHeaders != null) {
			this.allowHeaders = allowHeaders;
		}
	}

	public void destroy() {
	}
}