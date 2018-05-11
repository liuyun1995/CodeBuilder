package com.liuyun.builder.config.xml;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.liuyun.builder.codegen.util.XmlConstants;

//xml解析实例处理器
public class ParserEntityResolver implements EntityResolver {

    public ParserEntityResolver() {
        super();
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (XmlConstants.code_builder_id.equalsIgnoreCase(systemId)) {
            InputStream is = getClass().getClassLoader().getResourceAsStream("src/main/resources/codebuilder.dtd"); 
            InputSource ins = new InputSource(is);
            return ins;
        } else {
            return null;
        }
    }
}
