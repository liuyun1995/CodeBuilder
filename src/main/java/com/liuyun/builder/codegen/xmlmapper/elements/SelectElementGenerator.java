package com.liuyun.builder.codegen.xmlmapper.elements;

import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.TextElement;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.codegen.util.FormatUtil;

public class SelectElementGenerator extends AbstractXmlElementGenerator {

    public SelectElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
    	//添加<select>标签
        XmlElement answer = new XmlElement("select");
        //设置id属性
        answer.addAttribute(new Attribute("id", introspectedTable.getSelectStatementId())); 
        //设置resultMap属性
        if (introspectedTable.getRules().generateResultMap()) {
        	answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
        }
        String parameterType;
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            parameterType = introspectedTable.getPrimaryKeyType();
        } else {
            if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
                parameterType = "map"; 
            } else {
                parameterType = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().toString();
            }
        }
        //设置parameterType属性
        answer.addAttribute(new Attribute("parameterType", parameterType));
        
        context.getCommentGenerator().addComment(answer);
        //添加查询语句
        StringBuilder sb = new StringBuilder();
        sb.append("select "); 
        answer.addElement(new TextElement(sb.toString()));
        //获取基础列元素
        answer.addElement(getBaseColumnListElement());
        sb.setLength(0);
        sb.append("from ");
        sb.append(introspectedTable.getActualTableName());
        answer.addElement(new TextElement(sb.toString()));

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); 
            } else {
                sb.append("where "); 
                and = true;
            }
            sb.append(FormatUtil.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = "); 
            sb.append(FormatUtil.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        if (context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
