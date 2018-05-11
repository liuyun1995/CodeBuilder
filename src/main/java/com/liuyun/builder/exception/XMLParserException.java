package com.liuyun.builder.exception;

import java.util.ArrayList;
import java.util.List;


public class XMLParserException extends Exception {
	
    private static final long serialVersionUID = 5172525430401340573L;
    
    private List<String> errors;
    
    public XMLParserException(List<String> errors) {
        super();
        this.errors = errors;
    }
    
    public XMLParserException(String error) {
        super(error);
        this.errors = new ArrayList<String>();
        errors.add(error);
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    @Override
    public String getMessage() {
        if (errors != null && errors.size() > 0) {
            return errors.get(0);
        }
        return super.getMessage();
    }
    
}
