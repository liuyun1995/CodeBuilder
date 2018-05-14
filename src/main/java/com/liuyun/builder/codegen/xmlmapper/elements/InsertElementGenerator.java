package com.liuyun.builder.codegen.xmlmapper.elements;

import java.util.ArrayList;
import java.util.List;

import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.dom.OutputUtil;
import com.liuyun.builder.api.dom.java.FullyQualifiedJavaType;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.TextElement;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.codegen.util.FormatUtil;
import com.liuyun.builder.codegen.util.ListUtil;
import com.liuyun.builder.config.GeneratedKey;

//insert方法生成器
public class InsertElementGenerator extends AbstractXmlElementGenerator {

    private boolean isSimple;

    public InsertElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    //添加insert标签元素
    @Override
    public void addElements(XmlElement parentElement) {
    	//添加<insert>标签
        XmlElement answer = new XmlElement("insert"); 
        //设置id属性
        answer.addAttribute(new Attribute("id", introspectedTable.getInsertStatementId())); 
        //设置parameterType属性
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        //添加注释
        context.getCommentGenerator().addComment(answer);
        //获取主键
        GeneratedKey gk = introspectedTable.getGeneratedKey();
        if (gk != null) {
            IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn());
            if (introspectedColumn != null) {
                if (gk.isJdbcStandard()) {
                    answer.addAttribute(new Attribute("useGeneratedKeys", "true"));  
                    answer.addAttribute(new Attribute("keyProperty", introspectedColumn.getJavaProperty())); 
                    answer.addAttribute(new Attribute("keyColumn", introspectedColumn.getActualColumnName())); 
                } else {
                    answer.addElement(getSelectKey(introspectedColumn, gk));
                }
            }
        }
        StringBuilder insertClause = new StringBuilder();
        insertClause.append("insert into ");
        //插入表名
        insertClause.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" ("); 
        StringBuilder valuesClause = new StringBuilder();
        valuesClause.append("values ("); 
        List<String> valuesClauses = new ArrayList<String>();
        //获取该表的所有列
        List<IntrospectedColumn> columns = ListUtil.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        //遍历所有列
        for (int i = 0; i < columns.size(); i++) {
            IntrospectedColumn introspectedColumn = columns.get(i);
            insertClause.append(FormatUtil.getEscapedColumnName(introspectedColumn));
            valuesClause.append(FormatUtil.getParameterClause(introspectedColumn));
            if (i + 1 < columns.size()) {
                insertClause.append(", "); 
                valuesClause.append(", "); 
            }
            if (valuesClause.length() > 80) {
            	//添加XML文本元素
                answer.addElement(new TextElement(insertClause.toString()));
                insertClause.setLength(0);
                //缩进
                OutputUtil.xmlIndent(insertClause, 1);
                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                //缩进
                OutputUtil.xmlIndent(valuesClause, 1);
            }
        }
        insertClause.append(')');
        answer.addElement(new TextElement(insertClause.toString()));
        valuesClause.append(')');
        valuesClauses.add(valuesClause.toString());
        for (String clause : valuesClauses) {
            answer.addElement(new TextElement(clause));
        }
        if (context.getPlugins().sqlMapInsertElementGenerated(answer,introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
