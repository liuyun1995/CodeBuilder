package com.liuyun.builder.internal;

import static com.liuyun.builder.internal.utils.StringUtil.isTrue;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import javax.xml.bind.DatatypeConverter;
import com.liuyun.builder.internal.utils.StringUtil;
import com.liuyun.builder.api.CommentGenerator;
import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.MyBatisGenerator;
import com.liuyun.builder.api.dom.java.CompilationUnit;
import com.liuyun.builder.api.dom.java.Field;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.java.InnerClass;
import com.liuyun.builder.api.dom.java.InnerEnum;
import com.liuyun.builder.api.dom.java.JavaElement;
import com.liuyun.builder.api.dom.java.Method;
import com.liuyun.builder.api.dom.java.Parameter;
import com.liuyun.builder.api.dom.java.TopLevelClass;
import com.liuyun.builder.api.dom.xml.TextElement;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.config.MergeConstants;
import com.liuyun.builder.config.PropertyRegistry;

public class DefaultCommentGenerator implements CommentGenerator {

	private Properties properties;

	private boolean suppressDate;

	private boolean suppressAllComments;
	
	private boolean addRemarkComments;

	private SimpleDateFormat dateFormat;

	public DefaultCommentGenerator() {
		super();
		properties = new Properties();
		suppressDate = false;
		suppressAllComments = false;
		addRemarkComments = false;
	}

	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {}
	
	@Override
	public void addComment(XmlElement xmlElement) {
		if (suppressAllComments) {
			return;
		}
		xmlElement.addElement(new TextElement("<!--")); 
		StringBuilder sb = new StringBuilder();
		sb.append("  WARNING - "); 
		sb.append(MergeConstants.NEW_ELEMENT_TAG);
		xmlElement.addElement(new TextElement(sb.toString()));
		xmlElement.addElement(new TextElement("  This element is automatically generated by MyBatis Generator, do not modify.")); 
		String s = getDateString();
		if (s != null) {
			sb.setLength(0);
			sb.append("  This element was generated on "); 
			sb.append(s);
			sb.append('.');
			xmlElement.addElement(new TextElement(sb.toString()));
		}
		xmlElement.addElement(new TextElement("-->")); 
	}

	@Override
	public void addRootComment(XmlElement rootElement) {}

	@Override
	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);
		suppressDate = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
		suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
		addRemarkComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_ADD_REMARK_COMMENTS));
		String dateFormatString = properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_DATE_FORMAT);
		if (StringUtil.stringHasValue(dateFormatString)) {
			dateFormat = new SimpleDateFormat(dateFormatString);
		}
	}
	
	protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
		javaElement.addJavaDocLine(" *"); 
		StringBuilder sb = new StringBuilder();
		sb.append(" * "); 
		sb.append(MergeConstants.NEW_ELEMENT_TAG);
		if (markAsDoNotDelete) {
			sb.append(" do_not_delete_during_merge"); 
		}
		String s = getDateString();
		if (s != null) {
			sb.append(' ');
			sb.append(s);
		}
		javaElement.addJavaDocLine(sb.toString());
	}
	
	protected String getDateString() {
		if (suppressDate) {
			return null;
		} else if (dateFormat != null) {
			return dateFormat.format(new Date());
		} else {
			return new Date().toString();
		}
	}

	//添加类的注释
	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		innerClass.addJavaDocLine("/**"); 
		innerClass.addJavaDocLine(" * This class was generated by MyBatis Generator."); 
		sb.append(" * This class corresponds to the database table "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerClass.addJavaDocLine(sb.toString());
		addJavadocTag(innerClass, false);
		innerClass.addJavaDocLine(" */"); 
	}

	//添加类的注释
	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		innerClass.addJavaDocLine("/**"); 
		innerClass.addJavaDocLine(" * This class was generated by MyBatis Generator."); 
		sb.append(" * This class corresponds to the database table "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerClass.addJavaDocLine(sb.toString());
		addJavadocTag(innerClass, markAsDoNotDelete);
		innerClass.addJavaDocLine(" */"); 
	}

	//添加JavaModel的注释
	@Override
	public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (suppressAllComments || !addRemarkComments) {
			return;
		}
		topLevelClass.addJavaDocLine("/**"); 
		String remarks = introspectedTable.getRemarks();
		if (addRemarkComments && StringUtil.stringHasValue(remarks)) {
			topLevelClass.addJavaDocLine(" * Database Table Remarks:"); 
			String[] remarkLines = remarks.split(System.getProperty("line.separator")); 
			for (String remarkLine : remarkLines) {
				topLevelClass.addJavaDocLine(" *   " + remarkLine); 
			}
		}
		topLevelClass.addJavaDocLine(" *"); 
		topLevelClass.addJavaDocLine(" * This class was generated by MyBatis Generator."); 
		StringBuilder sb = new StringBuilder();
		sb.append(" * This class corresponds to the database table "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		topLevelClass.addJavaDocLine(sb.toString());
		topLevelClass.addJavaDocLine(" */"); 
	}

	@Override
	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		innerEnum.addJavaDocLine("/**"); 
		innerEnum.addJavaDocLine(" * This enum was generated by MyBatis Generator."); 
		sb.append(" * This enum corresponds to the database table "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerEnum.addJavaDocLine(sb.toString());
		addJavadocTag(innerEnum, false);
		innerEnum.addJavaDocLine(" */"); 
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
		field.addJavaDocLine("/**"); 
		String remarks = introspectedColumn.getRemarks();
		if (addRemarkComments && StringUtil.stringHasValue(remarks)) {
			field.addJavaDocLine(" * Database Column Remarks:"); 
			String[] remarkLines = remarks.split(System.getProperty("line.separator")); 
			for (String remarkLine : remarkLines) {
				field.addJavaDocLine(" *   " + remarkLine); 
			}
		}
		field.addJavaDocLine(" *"); 
		field.addJavaDocLine(" * This field was generated by MyBatis Generator."); 
		StringBuilder sb = new StringBuilder();
		sb.append(" * This field corresponds to the database column "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		sb.append('.');
		sb.append(introspectedColumn.getActualColumnName());
		field.addJavaDocLine(sb.toString());
		addJavadocTag(field, false);
		field.addJavaDocLine(" */"); 
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		field.addJavaDocLine("/**"); 
		field.addJavaDocLine(" * This field was generated by MyBatis Generator."); 
		sb.append(" * This field corresponds to the database table "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		field.addJavaDocLine(sb.toString());
		addJavadocTag(field, false);
		field.addJavaDocLine(" */"); 
	}

	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		method.addJavaDocLine("/**"); 
		method.addJavaDocLine(" * This method was generated by MyBatis Generator."); 
		sb.append(" * This method corresponds to the database table "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		method.addJavaDocLine(sb.toString());
		addJavadocTag(method, false);
		method.addJavaDocLine(" */"); 
	}

	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		method.addJavaDocLine("/**"); 
		method.addJavaDocLine(" * This method was generated by MyBatis Generator."); 
		sb.append(" * This method returns the value of the database column "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		sb.append('.');
		sb.append(introspectedColumn.getActualColumnName());
		method.addJavaDocLine(sb.toString());
		method.addJavaDocLine(" *"); 
		sb.setLength(0);
		sb.append(" * @return the value of "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		sb.append('.');
		sb.append(introspectedColumn.getActualColumnName());
		method.addJavaDocLine(sb.toString());
		addJavadocTag(method, false);
		method.addJavaDocLine(" */"); 
	}

	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		method.addJavaDocLine("/**"); 
		method.addJavaDocLine(" * This method was generated by MyBatis Generator."); 
		sb.append(" * This method sets the value of the database column "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		sb.append('.');
		sb.append(introspectedColumn.getActualColumnName());
		method.addJavaDocLine(sb.toString());
		method.addJavaDocLine(" *"); 
		Parameter parm = method.getParameters().get(0);
		sb.setLength(0);
		sb.append(" * @param "); 
		sb.append(parm.getName());
		sb.append(" the value for "); 
		sb.append(introspectedTable.getFullyQualifiedTable());
		sb.append('.');
		sb.append(introspectedColumn.getActualColumnName());
		method.addJavaDocLine(sb.toString());
		addJavadocTag(method, false);
		method.addJavaDocLine(" */"); 
	}

	@Override
	public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports) {
		imports.add(new FullyQualifiedJavaType("javax.annotation.Generated")); 
		String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString(); 
		method.addAnnotation(getGeneratedAnnotation(comment));
	}

	@Override
	public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
		imports.add(new FullyQualifiedJavaType("javax.annotation.Generated")); 
		String comment = "Source field: " 
				+ introspectedTable.getFullyQualifiedTable().toString() + "." 
				+ introspectedColumn.getActualColumnName();
		method.addAnnotation(getGeneratedAnnotation(comment));
	}

	@Override
	public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports) {
		imports.add(new FullyQualifiedJavaType("javax.annotation.Generated")); 
		String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString(); 
		field.addAnnotation(getGeneratedAnnotation(comment));
	}

	@Override
	public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
		imports.add(new FullyQualifiedJavaType("javax.annotation.Generated")); 
		String comment = "Source field: " 
				+ introspectedTable.getFullyQualifiedTable().toString() + "." 
				+ introspectedColumn.getActualColumnName();
		field.addAnnotation(getGeneratedAnnotation(comment));
		if (!suppressAllComments && addRemarkComments) {
			String remarks = introspectedColumn.getRemarks();
			if (addRemarkComments && StringUtil.stringHasValue(remarks)) {
				field.addJavaDocLine("/**"); 
				field.addJavaDocLine(" * Database Column Remarks:"); 
				String[] remarkLines = remarks.split(System.getProperty("line.separator")); 
				for (String remarkLine : remarkLines) {
					field.addJavaDocLine(" *   " + remarkLine); 
				}
				field.addJavaDocLine(" */"); 
			}
		}
	}

	@Override
	public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports) {
		imports.add(new FullyQualifiedJavaType("javax.annotation.Generated")); 
		String comment = "Source Table: " + introspectedTable.getFullyQualifiedTable().toString(); 
		innerClass.addAnnotation(getGeneratedAnnotation(comment));
	}

	private String getGeneratedAnnotation(String comment) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("@Generated("); 
		if (suppressAllComments) {
			buffer.append('\"');
		} else {
			buffer.append("value=\""); 
		}
		buffer.append(MyBatisGenerator.class.getName());
		buffer.append('\"');
		if (!suppressDate && !suppressAllComments) {
			buffer.append(", date=\""); 
			buffer.append(DatatypeConverter.printDateTime(Calendar.getInstance()));
			buffer.append('\"');
		}
		if (!suppressAllComments) {
			buffer.append(", comments=\""); 
			buffer.append(comment);
			buffer.append('\"');
		}
		buffer.append(')');
		return buffer.toString();
	}
	
}
