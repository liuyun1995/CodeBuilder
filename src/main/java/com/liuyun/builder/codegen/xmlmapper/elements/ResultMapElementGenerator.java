package com.liuyun.builder.codegen.xmlmapper.elements;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import java.util.List;
import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.codegen.util.FormatUtil;

public class ResultMapElementGenerator extends AbstractXmlElementGenerator {

    private boolean isSimple;

    public ResultMapElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addElements(XmlElement parentElement) {
    	//添加<resultMap>标签
        XmlElement answer = new XmlElement("resultMap");
        //设置id属性
        answer.addAttribute(new Attribute("id", introspectedTable.getBaseResultMapId()));
        
        //获取返回类型
        String returnType = introspectedTable.getBaseRecordType();
        
        //设置type属性
        answer.addAttribute(new Attribute("type", returnType));
        
        context.getCommentGenerator().addComment(answer);
        //添加其他子标签
        addResultMapElements(answer);
        
        if (context.getPlugins().sqlMapResultMapWithoutBLOBsElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    private void addResultMapElements(XmlElement answer) {
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            XmlElement resultElement = new XmlElement("id"); 
            resultElement.addAttribute(new Attribute("column", FormatUtil.getRenamedColumnNameForResultMap(introspectedColumn))); 
            resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty())); 
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            if (stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler())); 
            }
            answer.addElement(resultElement);
        }
        List<IntrospectedColumn> columns;
        if (isSimple) {
            columns = introspectedTable.getNonPrimaryKeyColumns();
        } else {
            columns = introspectedTable.getBaseColumns();
        }
        for (IntrospectedColumn introspectedColumn : columns) {
            XmlElement resultElement = new XmlElement("result"); 
            resultElement.addAttribute(new Attribute("column", FormatUtil.getRenamedColumnNameForResultMap(introspectedColumn))); 
            resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty())); 
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            if (stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler())); 
            }
            answer.addElement(resultElement);
        }
    }
    
}
