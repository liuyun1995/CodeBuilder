package com.liuyun.builder.codegen.xmlmapper.elements;

import java.util.Iterator;

import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.dom.OutputUtil;
import com.liuyun.builder.api.dom.xml.Attribute;
import com.liuyun.builder.api.dom.xml.TextElement;
import com.liuyun.builder.api.dom.xml.XmlElement;
import com.liuyun.builder.codegen.util.FormatUtil;
import com.liuyun.builder.codegen.util.ListUtil;


public class UpdateElementGenerator extends AbstractXmlElementGenerator {

    private boolean isSimple;

    public UpdateElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");
        //设置id属性
        answer.addAttribute(new Attribute("id", introspectedTable.getUpdateStatementId()));
        //设置parameterType属性
        answer.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        //添加注释
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        
        sb.setLength(0);
        sb.append("set "); 

        Iterator<IntrospectedColumn> iter;
        if (isSimple) {
            iter = ListUtil.removeGeneratedAlwaysColumns(introspectedTable.getNonPrimaryKeyColumns()).iterator();
        } else {
            iter = ListUtil.removeGeneratedAlwaysColumns(introspectedTable.getBaseColumns()).iterator();
        }
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            sb.append(FormatUtil.getEscapedColumnName(introspectedColumn));
            sb.append(" = "); 
            sb.append(FormatUtil.getParameterClause(introspectedColumn));
            if (iter.hasNext()) {
                sb.append(',');
            }
            answer.addElement(new TextElement(sb.toString()));
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtil.xmlIndent(sb, 1);
            }
        }

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); 
            } else {
                sb.append("where "); 
                and = true;
            }
            sb.append(FormatUtil.getEscapedColumnName(introspectedColumn));
            sb.append(" = "); 
            sb.append(FormatUtil.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        if (context.getPlugins().sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
