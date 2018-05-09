package com.liuyun.builder.codegen.javamapper;

import com.liuyun.builder.api.dom.java.Interface;
import com.liuyun.builder.codegen.core.AbstractXmlMapperGenerator;
import com.liuyun.builder.codegen.javamapper.elements.AbstractJavaMapperMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.annotated.AnnotatedDeleteMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.annotated.AnnotatedInsertMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.annotated.AnnotatedMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.annotated.AnnotatedUpdateMethodGenerator;


public class SimpleAnnotatedClientGenerator extends SimpleJavaClientGenerator {

    public SimpleAnnotatedClientGenerator() {
        super(false);
    }

    @Override
    protected void addInsertMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedInsertMethodGenerator(true);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addDeleteMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDelete()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedDeleteMethodGenerator(true);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addUpdateMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdate()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateMethodGenerator(true);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    @Override
    protected void addSelectMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelect()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedMethodGenerator(false, true);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    @Override
    public AbstractXmlMapperGenerator getMatchedXMLGenerator() {
        return null;
    }
}
