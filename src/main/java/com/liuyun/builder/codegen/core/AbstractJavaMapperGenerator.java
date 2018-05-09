package com.liuyun.builder.codegen.core;

//指定是否需要XML生成器
public abstract class AbstractJavaMapperGenerator extends AbstractJavaGenerator {
	
	//是否需要XML生成器
    private boolean requiresXMLGenerator;
    
    public AbstractJavaMapperGenerator(boolean requiresXMLGenerator) {
        super();
        this.requiresXMLGenerator = requiresXMLGenerator;
    }

    //是否需要XML生成器
    public boolean requiresXMLGenerator() {
        return requiresXMLGenerator;
    }
    
    //获取匹配的XML生成器
    public abstract AbstractXmlMapperGenerator getMatchedXMLGenerator();
    
}
