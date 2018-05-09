package com.liuyun.builder.api.dom.xml;

import com.liuyun.builder.api.dom.OutputUtil;

//XML文本元素
public class TextElement extends Element {

    //文本内容
    private String content;

    public TextElement(String content) {
        super();
        this.content = content;
    }

    //获取格式化内容
    @Override
    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        //缩进
        OutputUtil.xmlIndent(sb, indentLevel);
        //添加内容
        sb.append(content);
        return sb.toString();
    }

    public String getContent() {
        return content;
    }
}
