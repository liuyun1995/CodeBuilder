package com.liuyun.builder.codegen.javamapper;

import com.liuyun.builder.api.dom.java.Interface;
import com.liuyun.builder.codegen.core.AbstractXmlMapperGenerator;
import com.liuyun.builder.codegen.javamapper.elements.AbstractJavaMapperMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.annotated.AnnotatedDeleteMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.annotated.AnnotatedInsertMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.annotated.AnnotatedMethodGenerator;
import com.liuyun.builder.codegen.javamapper.elements.annotated.AnnotatedUpdateMethodGenerator;
import com.liuyun.builder.codegen.xmlmapper.MixedMapperGenerator;

public class MixedClientGenerator extends JavaMapperGenerator {

    public MixedClientGenerator() {
        super(true);
    }

    @Override
    protected void addInsertMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedInsertMethodGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addDeleteMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateDelete()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedDeleteMethodGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    @Override
    protected void addUpdateMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateUpdate()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedUpdateMethodGenerator(false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }
    
    @Override
    protected void addSelectMethod(Interface interfaze) {
        if (introspectedTable.getRules().generateSelect()) {
            AbstractJavaMapperMethodGenerator methodGenerator = new AnnotatedMethodGenerator(true, false);
            initializeAndExecuteGenerator(methodGenerator, interfaze);
        }
    }

    @Override
    public AbstractXmlMapperGenerator getMatchedXMLGenerator() {
        return new MixedMapperGenerator();
    }
}
