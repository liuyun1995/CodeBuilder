package com.liuyun.builder.codegen.core;

import com.liuyun.builder.api.dom.xml.Document;

//抽象XML生成器
public abstract class AbstractXmlMapperGenerator extends AbstractGenerator {
	
    public abstract Document getDocument();
    
}
