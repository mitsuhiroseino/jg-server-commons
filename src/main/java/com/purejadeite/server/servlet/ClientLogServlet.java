package com.purejadeite.server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * クライアントが送信したログをサーバーログとして出力するためのサーブレットです。
 *
 * @author mitsuhiroseino
 *
 */
public class ClientLogServlet extends HttpServlet {

	private static final long serialVersionUID = -3959765215487432107L;

	/**
	 * ロガー
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ClientLogServlet.class);

	/**
	 * デフォルトの日付フォーマット
	 */
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * デフォルトのタイムゾーン
	 */
	private static final String DATE_TIME_ZONE = "GMT";

	/**
	 * デフォルトのリクエスト型式
	 */
	private static final String REQUEST_TYPE_JSON = "json";

	public static final String INIT_SEVER_TIME_ZONE = "serverTimeZone";
	public static final String INIT_SEVER_DATE_FORMAT = "serverDateFormat";
	public static final String INIT_PARSE_ERROR_DATE_FORMAT = "parseErrorDateFormat";
	public static final String INIT_CLIENT_DATE_FORMAT = "clientDateFormat";
	public static final String INIT_REQUEST_TYPE = "requestType";
	public static final String INIT_LEVEL_PROPERTY = "levelProperty";
	public static final String INIT_TIMESTAMP_PROPERTY = "timestampProperty";
	public static final String INIT_MESSAGE_PROPERTY = "messageProperty";

	public static final String PROPERTY_LEVEL = "level";
	public static final String PROPERTY_TIMESTAMP = "timestamp";
	public static final String PROPERTY_MESSAGE = "message";

	public static final String PROPERTY_IP_ADDRESS = "ipAddress";
	public static final String PROPERTY_PORT = "port";
	public static final String PROPERTY_SESSION_ID = "sessionId";

	public static final String LEVEL_ERROR = "error";
	public static final String LEVEL_WARN = "warn";
	public static final String LEVEL_INFO = "info";
	public static final String LEVEL_DEBUG = "debug";
	public static final String LEVEL_TRACE = "trace";
	
	/**
	 * オブジェクトマッパー
	 */
	private static ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * サーバーでタイムスタンプを出力する為のフォーマッター
	 */
	private SimpleDateFormat serverDateFormatter = null;

	/**
	 * クライアントの日付けをパースできなかった際に、サーバーでタイムスタンプを出力する為のフォーマッター
	 */
	private SimpleDateFormat parseErrorDateFormatter = null;

	/**
	 * クライアントのタイムスタンプをパースする為のフォーマッター
	 */
	private SimpleDateFormat clientDateFormatter = null;
	
	private String requestType = REQUEST_TYPE_JSON;
	
	private String levelProperty = PROPERTY_LEVEL;
	
	private String timestampProperty = PROPERTY_TIMESTAMP;
	
	private String messageProperty = PROPERTY_MESSAGE;

	public ClientLogServlet() {
	}

	public void init() {
		// 初期化コンフィグを取得
		ServletConfig config = getServletConfig();

		// サーバーに保存する際の日付フォーマット
		String serverDateFormat = config
				.getInitParameter(INIT_SEVER_DATE_FORMAT);
		if (StringUtils.isEmpty(serverDateFormat)) {
			serverDateFormat = DATE_FORMAT;
		}
		serverDateFormatter = new SimpleDateFormat(serverDateFormat);

		// サーバーに保存する際の日付フォーマット
		String parseErrorDateFormat = config
				.getInitParameter(INIT_PARSE_ERROR_DATE_FORMAT);
		if (StringUtils.isEmpty(parseErrorDateFormat)) {
			parseErrorDateFormat = DATE_FORMAT;
		}
		parseErrorDateFormatter = new SimpleDateFormat(parseErrorDateFormat);

		// クライアントから受信する日付フォーマット
		String clientDateFormat = config
				.getInitParameter(INIT_CLIENT_DATE_FORMAT);
		if (StringUtils.isEmpty(clientDateFormat)) {
			clientDateFormat = DATE_FORMAT;
		}
		clientDateFormatter = new SimpleDateFormat(clientDateFormat);

		// サーバーのタイムゾーン
		String serverTimeZone = config.getInitParameter(INIT_SEVER_TIME_ZONE);
		if (StringUtils.isEmpty(serverTimeZone)) {
			serverTimeZone = DATE_TIME_ZONE;
		}
		clientDateFormatter.setTimeZone(TimeZone.getTimeZone(serverTimeZone));
		
		// リクエストの型式
		String requestType = config.getInitParameter(INIT_REQUEST_TYPE);
		if (!StringUtils.isEmpty(requestType)) {
			this.requestType = requestType;
		}
		
		// レベルの型式
		String levelProperty = config.getInitParameter(INIT_LEVEL_PROPERTY);
		if (!StringUtils.isEmpty(levelProperty)) {
			this.levelProperty = levelProperty;
		}
		
		// タイムスタンプの型式
		String timestampProperty = config.getInitParameter(INIT_TIMESTAMP_PROPERTY);
		if (!StringUtils.isEmpty(timestampProperty)) {
			this.timestampProperty = timestampProperty;
		}
		
		// メッセージの型式
		String messageProperty = config.getInitParameter(INIT_MESSAGE_PROPERTY);
		if (!StringUtils.isEmpty(messageProperty)) {
			this.messageProperty = messageProperty;
		}

	}

	/**
	 * GET時の処理
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setStatus(200);
	}

	/**
	 * POST時の処理
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		List<Map<String, String>> logs = null;
		
		if (REQUEST_TYPE_JSON.equalsIgnoreCase(this.requestType)) {
			// リクエストがJSONの場合
			logs = fromJson(request);
		} else {
			// TODO リクエストがForm Submitの場合
		}
		
		if (logs.isEmpty()) {
			return;
		}
		
		// サーバー側で取得できるクライアント情報の設定
		MDC.put(PROPERTY_IP_ADDRESS, request.getRemoteAddr());
		MDC.put(PROPERTY_PORT, String.valueOf(request.getRemotePort()));
		HttpSession session = request.getSession();
		if (session != null) {
			MDC.put(PROPERTY_SESSION_ID, session.getId());
		}
		// ログの出力
		for (Map<String, String> log : logs) {
			log(log);
		}
		// サーバー側で取得できるクライアント情報の削除
		MDC.remove(PROPERTY_IP_ADDRESS);
		MDC.remove(PROPERTY_PORT);
		MDC.remove(PROPERTY_SESSION_ID);
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> fromJson(HttpServletRequest request) throws IOException {
		// リクエストがJSONの場合
		BufferedReader reader = new BufferedReader(request.getReader());
		StringBuilder json = new StringBuilder();
		String str;
		while ((str = reader.readLine()) != null) {
			json.append(str);
		}
		if (json.length() != 0) {
			// Listに変換
			return (List<Map<String, String>>) MAPPER
					.readValue(json.toString(), List.class);
		} else {
			return Collections.EMPTY_LIST;
		}
	}
	
	/**
	 * ログを出力します。
	 * @param log
	 */
	private void log(Map<String, String> log) {
		setValues(log);
		String level = StringUtils.defaultString(log.get(levelProperty));
		String message = StringUtils.defaultString(log.get(messageProperty));
		if (LEVEL_ERROR.equals(level)) {
			LOGGER.error(message);
		} else if (LEVEL_WARN.equals(level)) {
			LOGGER.warn(message);
		} else if (LEVEL_INFO.equals(level)) {
			LOGGER.info(message);
		} else if (LEVEL_DEBUG.equals(level)) {
			LOGGER.debug(message);
		} else if (LEVEL_TRACE.equals(level)) {
			LOGGER.trace(message);
		} else {
			LOGGER.info(message);
		}
		clearValues(log);
	}

	/**
	 * ログに出力する値をMDCへ設定します
	 * @param log
	 */
	private void setValues(Map<String, String> log) {
		// timestampのみ変換を行う
		MDC.put(timestampProperty, getTimestamp(StringUtils.defaultString(log.get(timestampProperty))));
		// timestamp以外はそのままMDCへ設定
		for (String property : log.keySet()) {
			if (!timestampProperty.equals(property)) {
				MDC.put(property, StringUtils.defaultString(log.get(property)));
			}
		}
	}

	// timestampをクライアントのフォーマットから、サーバーのフォーマットへ変換します
	private String getTimestamp(String clientTimestamp) {
		Date timestamp = null;
		if (StringUtils.isEmpty(clientTimestamp)) {
			timestamp = new Date();
		} else {
			try {
				timestamp = clientDateFormatter.parse(clientTimestamp);
			} catch (ParseException e) {
				LOGGER.warn("The timestamp could not be parsed:" + clientTimestamp);
				return this.parseErrorDateFormatter.format(new Date());
			}
		}
		return this.serverDateFormatter.format(timestamp);
	}

	/**
	 * MDCに設定した値をクリアします
	 */
	private void clearValues(Map<String, String> log) {
		for (String property : log.keySet()) {
			MDC.remove(property);
		}
	}
}