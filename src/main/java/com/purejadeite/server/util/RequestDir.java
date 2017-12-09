package com.purejadeite.server.util;

import java.io.File;

public class RequestDir {

	private static ManagedDir dir;

	public static void setRoot(File root) {
		dir = ManagedDir.newInstance(root);
	}

	public static File create(String sessionId) {
		return dir.create(sessionId);
	}

	public static File get(String sessionId) {
		return dir.get(sessionId);
	}

	public static File get(String sessionId, String file) {
		return dir.get(sessionId, file);
	}

	public static boolean delete(String sessionId) {
		return dir.delete(sessionId);
	}

	public static boolean delete() {
		return dir.delete();
	}

}
