package com.liuyun.builder.api;

import com.liuyun.builder.api.dom.xml.Document;

//XML文件生成器
public class GeneratedXmlFile extends GeneratedFile {

    //XML文档
    private Document document;

    //文件名
    private String fileName;

    //目标路径
    private String targetPackage;

    //是否合并
    private boolean isMergeable;

    //XML格式化器
    private XmlFormatter xmlFormatter;

    public GeneratedXmlFile(Document document, String fileName,
            String targetPackage, String targetProject, boolean isMergeable,
            XmlFormatter xmlFormatter) {
        super(targetProject);
        this.document = document;
        this.fileName = fileName;
        this.targetPackage = targetPackage;
        this.isMergeable = isMergeable;
        this.xmlFormatter = xmlFormatter;
    }
    
    //获取格式化文本
    @Override
    public String getFormattedContent() {
        return xmlFormatter.getFormattedContent(document);
    }

    //获取文件名
    @Override
    public String getFileName() {
        return fileName;
    }

    //获取目标路径
    @Override
    public String getTargetPackage() {
        return targetPackage;
    }

    //是否可以合并
    @Override
    public boolean isMergeable() {
        return isMergeable;
    }

    public void setMergeable(boolean isMergeable) {
        this.isMergeable = isMergeable;
    }
    
}
