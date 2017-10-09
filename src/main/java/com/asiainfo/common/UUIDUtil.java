package com.asiainfo.common;

import java.util.UUID;

public class UUIDUtil {
	public static String getUUIDKey(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
