package com.xiaoju.hallowmas.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by dennyy on 16/8/4.
 */
public class StringUtils {

    /**
     * 有效的字符串,且字符串长度有相应限制
     *
     * @param str
     * @param maxLimit
     * @return
     */
    public static boolean isEffectiveStr(String str, int maxLimit) {
        return str != null && str.trim().length() > 0 && str.length() <= maxLimit;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 有效的字符串
     *
     * @param str
     * @return
     */
    public static boolean isEffectiveStr(String str) {
        return str != null && str.trim().length() > 0;
    }

    /**
     * 把List中的元素抽取出来,自身只加一对中括号
     *
     * @param list
     * @param <E>
     * @return
     */
    public static <E> String listToString(List<E> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(toString(list.get(i)));
            if (i < (list.size() - 1)) {
                sb.append(",");
            }
        }
        return sb.append("]").toString();
    }

    /**
     * Object.toString
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        try {

            Class clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();

            StringBuilder sb = new StringBuilder("{");
            for (Field field : fields) {
                if (field.getModifiers() == (Modifier.PUBLIC | Modifier.FINAL | Modifier.STATIC)) {
                    continue;
                }

                field.setAccessible(true);
                sb.append("\"").append(field.getName()).append("\":");

                Class typeClazz = field.getType();
                if (typeClazz == String.class) {
                    Object value = field.get(obj);
                    if (value == null) {
                        sb.append(value);
                    } else {
                        sb.append("\"").append(field.get(obj)).append("\"");
                    }

                } else {
                    sb.append(field.get(obj));
                }

                sb.append(",");
            }

            return sb.append("}").toString();
        } catch (Exception e) {
        }

        return obj.toString();
    }


}
