package com.liuyun.builder.api.dom.xml;

import com.liuyun.builder.api.dom.OutputUtil;

public class Document {
	
    private String publicId;
    
    private String systemId;

    //根结点
    private XmlElement rootElement;
    
    public Document(String publicId, String systemId) {
        super();
        this.publicId = publicId;
        this.systemId = systemId;
    }
    
    public Document() {
        super();
    }

    public XmlElement getRootElement() {
        return rootElement;
    }

    public void setRootElement(XmlElement rootElement) {
        this.rootElement = rootElement;
    }
    
    public String getPublicId() {
        return publicId;
    }

    public String getSystemId() {
        return systemId;
    }

    //获取格式化内容
    public String getFormattedContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); 
        if (publicId != null && systemId != null) {
            OutputUtil.newLine(sb);
            sb.append("<!DOCTYPE "); 
            sb.append(rootElement.getName());
            sb.append(" PUBLIC \""); 
            sb.append(publicId);
            sb.append("\" \"");
            sb.append(systemId);
            sb.append("\">"); 
        }
        OutputUtil.newLine(sb);
        //添加根结点后面的格式化内容
        sb.append(rootElement.getFormattedContent(0));
        return sb.toString();
    }
    
}
