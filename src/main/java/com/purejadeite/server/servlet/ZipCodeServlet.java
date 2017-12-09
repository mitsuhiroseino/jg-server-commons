package com.purejadeite.server.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 郵便番号 or 住所を取得する為のサーブレットです。
 *
 * @author mitsuhiroseino
 *
 */
public class ZipCodeServlet extends HttpServlet {

	private static final long serialVersionUID = 2044630730164743681L;

	/**
	 * ロガー
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZipCodeServlet.class);

	/**
	 * オブジェクトマッパー
	 */
	private static ObjectMapper MAPPER = new ObjectMapper();

	public ZipCodeServlet() {
	}

	public void init() {
		// 初期化コンフィグを取得
		ServletConfig config = getServletConfig();
	}

	/**
	 * GET時の処理
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// リクエストに応じた検索結果を返す
	}

	/**
	 * POST時の処理
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// JPのHPより郵便番号ファイルを取得する
	}

}