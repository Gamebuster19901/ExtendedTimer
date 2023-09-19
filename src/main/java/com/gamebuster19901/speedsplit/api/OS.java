package com.gamebuster19901.speedsplit.api;

import org.apache.commons.lang3.SystemUtils;

public enum OS {
	LINUX,
	WINDOWS;
	
	private static final OS os;
	static {
		if(SystemUtils.IS_OS_LINUX) {
			os = LINUX;
		}
		else if (SystemUtils.IS_OS_WINDOWS) {
			os = WINDOWS;
		}
		else {
			throw new Error("Cannot run on " + SystemUtils.OS_NAME);
		}
	}
	
	public static final OS getOS() {
		return os;
	}
	
}
