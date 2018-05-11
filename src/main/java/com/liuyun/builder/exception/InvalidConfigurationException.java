package com.liuyun.builder.exception;

import java.util.List;

public class InvalidConfigurationException extends Exception {
	
    static final long serialVersionUID = 4902307610148543411L;
    
    private List<String> errors;
    
    public InvalidConfigurationException(List<String> errors) {
        super();
        this.errors = errors;
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
