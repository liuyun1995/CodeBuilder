package com.liuyun.builder.api;

import com.liuyun.builder.api.dom.xml.Document;

//XML文件生成器
public class GeneratedXmlFile extends GeneratedFile {

    private Document document;           //xml文档
    private String fileName;             //文件名
    private boolean isMergeable;        //是否合并
    private XmlFormatter xmlFormatter;   //格式化器

    public GeneratedXmlFile(Document document, String fileName,
    		String targetProject, String targetPackage, boolean isMergeable,
            XmlFormatter xmlFormatter) {
        super(targetProject, targetPackage);
        this.document = document;
        this.fileName = fileName;
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

    //获取项目路径
    @Override
	public String getTargetProject() {
		return targetProject;
	}
    
	//获取包路径
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
