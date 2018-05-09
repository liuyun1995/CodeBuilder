package com.liuyun.builder.internal.utils;

import java.util.StringTokenizer;

public class StringUtil {

    private StringUtil() {
        super();
    }

    public static boolean stringHasValue(String s) {
        return s != null && s.length() > 0;
    }

    //生成全限定表名
    public static String composeFullyQualifiedTableName(String catalog, String schema, String tableName, char separator) {
        StringBuilder sb = new StringBuilder();
        if (stringHasValue(catalog)) {
            sb.append(catalog);
            sb.append(separator);
        }
        if (stringHasValue(schema)) {
            sb.append(schema);
            sb.append(separator);
        } else {
            if (sb.length() > 0) {
                sb.append(separator);
            }
        }
        sb.append(tableName);
        return sb.toString();
    }

    //判断字符串是否包含空格
    public static boolean stringContainsSpace(String s) {
        return s != null && s.indexOf(' ') != -1;
    }

    public static String escapeStringForJava(String s) {
        StringTokenizer st = new StringTokenizer(s, "\"", true); 
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("\"".equals(token)) { 
                sb.append("\\\""); 
            } else {
                sb.append(token);
            }
        }
        return sb.toString();
    }

    public static String escapeStringForXml(String s) {
        StringTokenizer st = new StringTokenizer(s, "\"", true); 
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("\"".equals(token)) { 
                sb.append("&quot;"); 
            } else {
                sb.append(token);
            }
        }
        return sb.toString();
    }

    public static boolean isTrue(String s) {
        return "true".equalsIgnoreCase(s); 
    }

    public static boolean stringContainsSQLWildcard(String s) {
        if (s == null) {
            return false;
        }
        return s.indexOf('%') != -1 || s.indexOf('_') != -1;
    }
}
