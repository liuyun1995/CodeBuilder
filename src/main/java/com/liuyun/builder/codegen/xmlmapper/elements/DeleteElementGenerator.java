package com.liuyun.builder.codegen.xmlmapper.elements;

import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.TextElement;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.codegen.util.FormatUtil;

public class DeleteElementGenerator extends AbstractXmlElementGenerator {

    private boolean isSimple;

    public DeleteElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addElements(XmlElement parentElement) {
    	//添加<delete>标签
        XmlElement answer = new XmlElement("delete");
        //设置id属性
        answer.addAttribute(new Attribute("id", introspectedTable.getDeleteStatementId())); 
        String parameterClass;
        if (!isSimple && introspectedTable.getRules().generatePrimaryKeyClass()) {
        	//获取主键类型
            parameterClass = introspectedTable.getPrimaryKeyType();
        } else {
        	//如果主键大于1个
            if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
                parameterClass = "map"; 
            } else {
                parameterClass = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().toString();
            }
        }
        //设置parameterType属性
        answer.addAttribute(new Attribute("parameterType", parameterClass));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("delete from "); 
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
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
            sb.append(FormatUtil.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(FormatUtil.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        if (context.getPlugins().sqlMapDeleteByPrimaryKeyElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
