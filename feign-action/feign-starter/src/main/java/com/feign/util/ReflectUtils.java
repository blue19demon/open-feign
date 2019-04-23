package com.feign.util;

import java.lang.reflect.Method;

public class ReflectUtils {

	public static Object invokeGet(Object obj, String keyFiledName) {
		Class<? extends Object> c = obj.getClass();
		Method m = null;
		try {
			m = c.getDeclaredMethod("get" + StringUtils.fistCharUpperCase(keyFiledName));
			return m.invoke(obj, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object invokeSet(Object obj, String filedName, String fieldType, Object value) {
		Class<? extends Object> c = obj.getClass();
		Method m = null;
		try {
			m = c.getDeclaredMethod("set" + StringUtils.fistCharUpperCase(filedName), Class.forName(fieldType));
			return m.invoke(obj, new Object[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �ж�object�Ƿ�Ϊ��������
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isBaseType(Object object) {
		if(object==null) {
			return true;
		}
		Class<? extends Object> className = object.getClass();
		if (className.equals(java.lang.String.class) || className.equals(java.lang.Integer.class)
				|| className.equals(java.lang.Byte.class) || className.equals(java.lang.Long.class)
				|| className.equals(java.lang.Double.class) || className.equals(java.lang.Float.class)
				|| className.equals(java.lang.Character.class) || className.equals(java.lang.Short.class)
				|| className.equals(java.lang.Boolean.class)
				|| className.equals(java.util.Date.class)) {
			return true;
		}
		return false;
	}
}
