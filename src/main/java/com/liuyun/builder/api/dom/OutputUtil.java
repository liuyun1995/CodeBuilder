package com.liuyun.builder.api.dom;

import java.util.Set;
import java.util.TreeSet;

import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;

//输出工具类
public class OutputUtil {

    //换行符
    private static final String lineSeparator;

    static {
        String ls = System.getProperty("line.separator"); 
        if (ls == null) {
            ls = "\n"; 
        }
        lineSeparator = ls;
    }
    
    private OutputUtil() {
        super();
    }

    //java代码缩进
    public static void javaIndent(StringBuilder sb, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            sb.append("    "); 
        }
    }

    //xml文件缩进
    public static void xmlIndent(StringBuilder sb, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            sb.append("  "); 
        }
    }

    //换行操作
    public static void newLine(StringBuilder sb) {
        sb.append(lineSeparator);
    }

    //生成Import字符串
    public static Set<String> calculateImports(Set<FullyQualifiedJavaType> importedTypes) {
        StringBuilder sb = new StringBuilder();
        Set<String> importStrings = new TreeSet<String>();
        for (FullyQualifiedJavaType fqjt : importedTypes) {
            for (String importString : fqjt.getImportList()) {
                sb.setLength(0);
                sb.append("import "); 
                sb.append(importString);
                sb.append(';');
                importStrings.add(sb.toString());
            }
        }
        return importStrings;
    }
    
}
