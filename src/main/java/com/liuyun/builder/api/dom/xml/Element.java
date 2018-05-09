package com.liuyun.builder.api.dom.xml;

public abstract class Element {

    public Element() {
        super();
    }

    public abstract String getFormattedContent(int indentLevel);
    
}
