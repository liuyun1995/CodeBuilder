package com.liuyun.builder.codegen.xmlmapper;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import org.mybatis.generator.codegen.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.codegen.xmlmapper.elements.BaseColumnListElementGenerator;
import org.mybatis.generator.codegen.xmlmapper.elements.DeleteElementGenerator;
import org.mybatis.generator.codegen.xmlmapper.elements.InsertElementGenerator;
import org.mybatis.generator.codegen.xmlmapper.elements.ResultMapElementGenerator;
import org.mybatis.generator.codegen.xmlmapper.elements.SelectElementGenerator;
import org.mybatis.generator.codegen.xmlmapper.elements.UpdateElementGenerator;

import com.liuyun.builder.api.FullyQualifiedTable;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.Document;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.codegen.core.AbstractXmlMapperGenerator;
import com.liuyun.builder.codegen.util.XmlConstants;

//XML生成器
public class XMLMapperGenerator extends AbstractXmlMapperGenerator {

    public XMLMapperGenerator() {
        super();
    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.12", table.toString())); 
        XmlElement answer = new XmlElement("mapper"); 
        String namespace = introspectedTable.getSqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));

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

    protected void addResultMapElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateResultMap()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void addBaseColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseColumnList()) {
            AbstractXmlElementGenerator elementGenerator = new BaseColumnListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addInsertElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractXmlElementGenerator elementGenerator = new InsertElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addDeleteElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateDelete()) {
            AbstractXmlElementGenerator elementGenerator = new DeleteElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addUpdateElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdate()) {
            AbstractXmlElementGenerator elementGenerator = new UpdateElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addSelectElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelect()) {
            AbstractXmlElementGenerator elementGenerator = new SelectElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
    
    protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator, XmlElement parentElement) {
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.setProgressCallback(progressCallback);
        elementGenerator.setWarnings(warnings);
        elementGenerator.addElements(parentElement);
    }

    @Override
    public Document getDocument() {
        Document document = new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElement());

        if (!context.getPlugins().sqlMapDocumentGenerated(document, introspectedTable)) {
            document = null;
        }

        return document;
    }
}
