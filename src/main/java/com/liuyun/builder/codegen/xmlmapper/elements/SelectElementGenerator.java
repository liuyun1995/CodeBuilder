package com.liuyun.builder.codegen.xmlmapper.elements;

import static org.mybatis.generator.internal.util.StringUtil.stringHasValue;

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
        XmlElement answer = new XmlElement("select");
        //设置id属性
        answer.addAttribute(new Attribute("id", introspectedTable.getSelectStatementId())); 
        //设置resultMap属性
        if (introspectedTable.getRules().generateResultMap()) {
        	answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
        }
        //设置parameterType属性
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
        answer.addAttribute(new Attribute("parameterType", parameterType));
        
        //添加注释
        context.getCommentGenerator().addComment(answer);
        
        StringBuilder sb = new StringBuilder();
        sb.append("select "); 
//        if (stringHasValue(introspectedTable.getSelectByPrimaryKeyQueryId())) {
//            sb.append('\'');
//            sb.append(introspectedTable.getSelectByPrimaryKeyQueryId());
//            sb.append("' as QUERYID,"); 
//        }
        answer.addElement(new TextElement(sb.toString()));
        //获取基础列元素
        answer.addElement(getBaseColumnListElement());
        if (introspectedTable.hasBLOBColumns()) {
            //answer.addElement(new TextElement(",")); 
            //answer.addElement(getBlobColumnListElement());
        }
        sb.setLength(0);
        sb.append("from "); 
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
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
        //插件方法
        if (context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}