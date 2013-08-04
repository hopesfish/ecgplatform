package com.ainia.ecgApi.core.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * <p>编码解码工具类</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * EncodeUtils.java
 * @author pq
 * @createdDate 2013-6-29
 * @version 0.1
 */
public class EncodeUtils {

	/**
	 * <p>HEX 编码</p>
	 * @param input
	 * @return
	 * String
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * <p>HES 解码</p>
	 * @param input
	 * @return
	 * byte[]
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
	/**
	 * <p>Base64 编码</p>
	 * @param input
	 * @return
	 * String
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}

	/**
	 * <p>Base64解码</p>
	 * @param input
	 * @return
	 * byte[]
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}
	
	/**
	 * <p>html 编码</p>
	 * @param html
	 * @return
	 * String
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * <p>html 解码</p>
	 * @param htmlEscaped
	 * @return
	 * String
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}
	/**
	 * <p>获得指定字符串所有的ascii码的和值</p>
	 * @param str
	 * @return
	 * String
	 */
	public static String asciiSum(String str) {
		int sum = 0;
		for (char c : str.toCharArray()) {
			sum += (int)c;
		}
		return String.valueOf(sum);
	}
}
