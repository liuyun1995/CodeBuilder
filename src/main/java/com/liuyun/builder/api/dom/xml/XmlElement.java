package com.liuyun.builder.api.dom.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.liuyun.builder.api.dom.OutputUtil;

//XML标签元素
public class XmlElement extends Element {

    //属性集合
    private List<Attribute> attributes;

    //子节点集合
    private List<Element> elements;

    //标签名
    private String name;
    
    public XmlElement(String name) {
        super();
        attributes = new ArrayList<Attribute>();
        elements = new ArrayList<Element>();
        this.name = name;
    }
    
    public XmlElement(XmlElement original) {
        super();
        attributes = new ArrayList<Attribute>();
        attributes.addAll(original.attributes);
        elements = new ArrayList<Element>();
        elements.addAll(original.elements);
        this.name = original.name;
    }
    
    public List<Attribute> getAttributes() {
        return attributes;
    }
    
    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }
    
    public List<Element> getElements() {
        return elements;
    }
    
    public void addElement(Element element) {
        elements.add(element);
    }
    
    public void addElement(int index, Element element) {
        elements.add(index, element);
    }
    
    public String getName() {
        return name;
    }
    
    //生成格式化内容
    @Override
    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        //缩进
        OutputUtil.xmlIndent(sb, indentLevel);
        //添加头标签
        sb.append('<');
        sb.append(name);
        //添加属性
        Collections.sort(attributes);
        for (Attribute att : attributes) {
            sb.append(' ');
            sb.append(att.getFormattedContent());
        }
        if (elements.size() > 0) {
            sb.append(">");
            //添加每个子节点
            for (Element element : elements) {
                OutputUtil.newLine(sb);
                sb.append(element.getFormattedContent(indentLevel + 1));
            }
            //换行
            OutputUtil.newLine(sb);
            //缩进
            OutputUtil.xmlIndent(sb, indentLevel);
            //添加尾标签
            sb.append("</");
            sb.append(name);
            sb.append('>');
        } else {
            sb.append(" />");
        }
        return sb.toString();
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
