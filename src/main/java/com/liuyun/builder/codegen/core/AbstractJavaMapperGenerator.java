package com.liuyun.builder.codegen.core;

//指定是否需要XML生成器
public abstract class AbstractJavaMapperGenerator extends AbstractJavaGenerator {
	
	//是否需要XML生成器
    private boolean requiresXMLGenerator;
    
    public AbstractJavaMapperGenerator(boolean requiresXMLGenerator) {
        super();
        this.requiresXMLGenerator = requiresXMLGenerator;
    }

    public boolean requiresXMLGenerator() {
        return requiresXMLGenerator;
    }
    
    public abstract AbstractXmlMapperGenerator getMatchedXMLGenerator();
    
}
