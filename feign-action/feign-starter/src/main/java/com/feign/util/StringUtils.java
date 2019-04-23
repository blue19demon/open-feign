package com.feign.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static String fistCharUpperCase(String src) {
		return src.substring(0, 1).toUpperCase() + src.substring(1);
	}
	
	public static String str2Package(String src) {
		return src.substring(0, src.length()-".xml".length()).replaceAll("/", ".");
	}
	
	public static String get$StrValue(String src) {
		Pattern p = Pattern.compile("\\{[^\\}]*\\}");
		Matcher m = p.matcher(src);
		String rs=null;
		while (m.find()) {
			String item=src.substring(m.start(), m.end());
			rs=item.substring(1, item.length()-1);
		}
		return rs;
	}
	public static void main(String[] args) {
		System.out.println(str2Package("com/batis/mapper/UserMapper.xml"));
	}
}
