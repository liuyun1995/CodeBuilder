package com.liuyun.builder.api.dom.java;

//访问标志符
public enum JavaVisibility {
	
    PUBLIC("public "),
    PRIVATE("private "),
    PROTECTED("protected "),
    DEFAULT("");

    private String value;

    private JavaVisibility(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
}
