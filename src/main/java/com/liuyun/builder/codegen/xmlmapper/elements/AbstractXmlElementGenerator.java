package com.liuyun.builder.codegen.xmlmapper.elements;

import org.mybatis.generator.config.GeneratedKey;

import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.TextElement;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.codegen.core.AbstractGenerator;

//抽象的xml元素生成器
public abstract class AbstractXmlElementGenerator extends AbstractGenerator {
	
	//添加xml元素
    public abstract void addElements(XmlElement parentElement);

    public AbstractXmlElementGenerator() {
        super();
    }

    protected XmlElement getSelectKey(IntrospectedColumn introspectedColumn, GeneratedKey generatedKey) {
        String identityColumnType = introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName();
        XmlElement answer = new XmlElement("selectKey"); 
        answer.addAttribute(new Attribute("resultType", identityColumnType)); 
        answer.addAttribute(new Attribute("keyProperty", introspectedColumn.getJavaProperty())); 
        answer.addAttribute(new Attribute("order", generatedKey.getMyBatis3Order())); 
        answer.addElement(new TextElement(generatedKey.getRuntimeSqlStatement()));
        return answer;
    }

    //获取BaseColumnList元素
    protected XmlElement getBaseColumnListElement() {
        XmlElement answer = new XmlElement("include"); 
        answer.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
        return answer;
    }
    
}
