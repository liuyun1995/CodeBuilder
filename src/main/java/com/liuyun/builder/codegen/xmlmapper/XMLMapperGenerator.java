package com.liuyun.builder.codegen.xmlmapper;

import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import com.liuyun.builder.api.FullyQualifiedTable;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.Document;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.codegen.core.AbstractXmlMapperGenerator;
import com.liuyun.builder.codegen.util.XmlConstants;
import com.liuyun.builder.codegen.xmlmapper.elements.AbstractXmlElementGenerator;
import com.liuyun.builder.codegen.xmlmapper.elements.BaseColumnListElementGenerator;
import com.liuyun.builder.codegen.xmlmapper.elements.DeleteElementGenerator;
import com.liuyun.builder.codegen.xmlmapper.elements.InsertElementGenerator;
import com.liuyun.builder.codegen.xmlmapper.elements.ResultMapElementGenerator;
import com.liuyun.builder.codegen.xmlmapper.elements.SelectElementGenerator;
import com.liuyun.builder.codegen.xmlmapper.elements.UpdateElementGenerator;

//XML生成器
public class XMLMapperGenerator extends AbstractXmlMapperGenerator {

    public XMLMapperGenerator() {
        super();
    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.12", table.toString())); 
        XmlElement answer = new XmlElement("mapper"); 
        String namespace = introspectedTable.getXmlMapperNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));
        //添加备注
        context.getCommentGenerator().addRootComment(answer);
        //添加各种元素
        addResultMapElement(answer);
        addBaseColumnListElement(answer);
        addInsertElement(answer);
        addDeleteElement(answer);
        addUpdateElement(answer);
        addSelectElement(answer);
        return answer;
    }

    //添加ResultMap元素
    protected void addResultMapElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateResultMap()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    //添加BaseColumnList元素
    protected void addBaseColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseColumnList()) {
            AbstractXmlElementGenerator elementGenerator = new BaseColumnListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    //添加insert元素
    protected void addInsertElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractXmlElementGenerator elementGenerator = new InsertElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    //添加delete元素
    protected void addDeleteElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateDelete()) {
            AbstractXmlElementGenerator elementGenerator = new DeleteElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    //添加update元素
    protected void addUpdateElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdate()) {
            AbstractXmlElementGenerator elementGenerator = new UpdateElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    //添加select元素
    protected void addSelectElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelect()) {
            AbstractXmlElementGenerator elementGenerator = new SelectElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    //初始化生成器
    protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator, XmlElement parentElement) {
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.setProgressCallback(progressCallback);
        elementGenerator.setWarnings(warnings);
        elementGenerator.addElements(parentElement);
    }

    //获取xml文档
    @Override
    public Document getDocument() {
    	//新建Document
        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        //设置全部元素
        document.setRootElement(getSqlMapElement());
        if (!context.getPlugins().sqlMapDocumentGenerated(document, introspectedTable)) {
            document = null;
        }
        return document;
    }
}
