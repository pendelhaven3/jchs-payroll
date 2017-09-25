package com.pj.hrapp.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlUtil {

	private static final String UTF8 = "UTF-8";
	
	public static String mapToQueryString(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for (HashMap.Entry<String, String> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append('&');
			}
			try {
				sb.append(URLEncoder.encode(entry.getKey(), UTF8)).append('=')
						.append(URLEncoder.encode(entry.getValue(), UTF8));
			} catch (UnsupportedEncodingException e1) {
				throw new RuntimeException(e1);
			}
		}
		return sb.toString();
	}

	public static String mapToQueryString(String paramName, List<Long> longValues) {
		StringBuilder sb = new StringBuilder();
		for (Long longValue : longValues) {
			if (sb.length() > 0) {
				sb.append('&');
			}
			try {
				sb.append(URLEncoder.encode(paramName, UTF8)).append('=').append(longValue.toString());
			} catch (UnsupportedEncodingException e1) {
				throw new RuntimeException(e1);
			}
		}
		return sb.toString();
	}
	
}
