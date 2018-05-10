package com.liuyun.builder.internal.utils;

import static com.liuyun.builder.internal.utils.StringUtil.isTrue;

import java.util.Locale;
import java.util.Properties;

import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.dom.java.Field;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.JavaVisibility;
import com.liuyun.builder.api.dom.java.Method;
import com.liuyun.builder.api.dom.java.Parameter;
import com.liuyun.builder.config.PropertyRegistry;
import com.liuyun.builder.config.label.Context;
import com.liuyun.builder.config.label.TablesConfiguration;


public class JavaBeansUtil {

    private JavaBeansUtil() {
        super();
    }
    
    //获取getter方法名
    public static String getGetterMethodName(String property, FullyQualifiedJavaType fullyQualifiedJavaType) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        if (fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getBooleanPrimitiveInstance())) {
            sb.insert(0, "is"); 
        } else {
            sb.insert(0, "get"); 
        }
        return sb.toString();
    }
    
    //获取setter方法名
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "set");
        return sb.toString();
    }

    //获取驼峰字符串
    public static String getCamelCaseString(String inputString, boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            switch (c) {
            case '_':
            case '-':
            case '@':
            case '$':
            case '#':
            case ' ':
            case '/':
            case '&':
                if (sb.length() > 0) {
                    nextUpperCase = true;
                }
                break;
            default:
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
                break;
            }
        }
        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        return sb.toString();
    }
    
    //获取有效的属性名
    public static String getValidPropertyName(String inputString) {
        String answer;
        if (inputString == null) {
            answer = null;
        } else if (inputString.length() < 2) {
        	//如果字符串长度小于2则转成小写
            answer = inputString.toLowerCase(Locale.US);
        } else {
        	//如果第一个字符大写, 并且第二个字符小写
            if (Character.isUpperCase(inputString.charAt(0)) && !Character.isUpperCase(inputString.charAt(1))) {
                //将第一个字符转成小写
            	answer = inputString.substring(0, 1).toLowerCase(Locale.US) + inputString.substring(1);
            } else {
                answer = inputString;
            }
        }
        return answer;
    }

    public static Method getJavaBeansGetter(IntrospectedColumn introspectedColumn, Context context, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(fqjt);
        method.setName(getGetterMethodName(property, fqjt));
        context.getCommentGenerator().addGetterComment(method, introspectedTable, introspectedColumn);
        StringBuilder sb = new StringBuilder();
        sb.append("return "); 
        sb.append(property);
        sb.append(';');
        method.addBodyLine(sb.toString());
        return method;
    }

    public static Field getJavaBeansField(IntrospectedColumn introspectedColumn, Context context, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(fqjt);
        field.setName(property);
        context.getCommentGenerator().addFieldComment(field, introspectedTable, introspectedColumn);
        return field;
    }

    public static Method getJavaBeansSetter(IntrospectedColumn introspectedColumn, Context context, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(getSetterMethodName(property));
        method.addParameter(new Parameter(fqjt, property));
        context.getCommentGenerator().addSetterComment(method, introspectedTable, introspectedColumn);
        StringBuilder sb = new StringBuilder();
        if (introspectedColumn.isStringColumn() && isTrimStringsEnabled(introspectedColumn)) {
            sb.append("this."); 
            sb.append(property);
            sb.append(" = "); 
            sb.append(property);
            sb.append(" == null ? null : "); 
            sb.append(property);
            sb.append(".trim();"); 
            method.addBodyLine(sb.toString());
        } else {
            sb.append("this."); 
            sb.append(property);
            sb.append(" = "); 
            sb.append(property);
            sb.append(';');
            method.addBodyLine(sb.toString());
        }
        return method;
    }

    private static boolean isTrimStringsEnabled(Context context) {
        Properties properties = context.getJavaModelGeneratorConfiguration().getProperties();
        boolean rc = isTrue(properties.getProperty(PropertyRegistry.MODEL_GENERATOR_TRIM_STRINGS));
        return rc;
    }

    private static boolean isTrimStringsEnabled(IntrospectedTable table) {
        TablesConfiguration tableConfiguration = table.getTableConfiguration();
        String trimSpaces = tableConfiguration.getProperties().getProperty(PropertyRegistry.MODEL_GENERATOR_TRIM_STRINGS);
        if (trimSpaces != null) {
            return isTrue(trimSpaces); 
        }
        return isTrimStringsEnabled(table.getContext());
    }

    private static boolean isTrimStringsEnabled(IntrospectedColumn column) {
        String trimSpaces = column.getProperties().getProperty(PropertyRegistry.MODEL_GENERATOR_TRIM_STRINGS);
        if (trimSpaces != null) {
            return isTrue(trimSpaces);
        }
        return isTrimStringsEnabled(column.getIntrospectedTable());
    }
}
