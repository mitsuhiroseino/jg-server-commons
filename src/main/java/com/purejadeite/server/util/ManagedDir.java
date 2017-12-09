package com.purejadeite.server.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class ManagedDir {

	private File rootDir;
	
	private static Object LOCK = new Object();
	
	private static Map<File, ManagedDir> managedDirs = new HashMap<>();
	
	public static ManagedDir newInstance(File rootDir) {
		synchronized(LOCK) {
			ManagedDir managedDir = new ManagedDir(rootDir);
			managedDirs.put(rootDir, managedDir);
			return managedDir;
		}
	}
	
	public static void destroy() {
		synchronized(LOCK) {
			for (ManagedDir managedDir : managedDirs.values()) {
				managedDir.delete();
			}
			managedDirs.clear();
		}
	}
	
	private ManagedDir(File rootDir) {
		super();
		this.rootDir = rootDir;
	}

	public File create(String id) {
		File dir = get(id);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	public File get(String id) {
		return new File(rootDir, id);
	}

	public File get(String id, String file) {
		return new File(create(id), file);
	}

	public boolean delete(String id) {
		return delete(get(id));
	}

	public boolean delete() {
		return delete(rootDir);
	}

	private boolean delete(File dir) {
		try {
			FileUtils.deleteDirectory(dir);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public boolean clear() {
		try {
			FileUtils.cleanDirectory(rootDir);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
