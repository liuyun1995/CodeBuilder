package com.liuyun.builder.config.xml;

import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.List;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//xml解析错误处理器
public class ParserErrorHandler implements ErrorHandler {
	
    private List<String> warnings;
    
    private List<String> errors;
    
    public ParserErrorHandler(List<String> warnings, List<String> errors) {
        super();
        this.warnings = warnings;
        this.errors = errors;
    }
    
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        warnings.add(getString("Warning.7", Integer.toString(exception.getLineNumber()), exception.getMessage()));
    }
    
    @Override
    public void error(SAXParseException exception) throws SAXException {
        errors.add(getString("RuntimeError.4", Integer.toString(exception.getLineNumber()), exception.getMessage()));
    }
    
    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        errors.add(getString("RuntimeError.4", Integer.toString(exception.getLineNumber()), exception.getMessage()));
    }
    
}
