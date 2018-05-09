package com.liuyun.builder.codegen.util;

import java.util.ArrayList;
import java.util.List;

import com.liuyun.builder.api.IntrospectedColumn;

//List工具类
public class ListUtil {

    public static List<IntrospectedColumn> removeGeneratedAlwaysColumns(List<IntrospectedColumn> columns) {
        List<IntrospectedColumn> filteredList = new ArrayList<IntrospectedColumn>();
        for (IntrospectedColumn ic : columns) {
            if (!ic.isGeneratedAlways()) {
                filteredList.add(ic);
            }
        }
        return filteredList;
    }

    public static List<IntrospectedColumn> removeIdentityAndGeneratedAlwaysColumns(List<IntrospectedColumn> columns) {
        List<IntrospectedColumn> filteredList = new ArrayList<IntrospectedColumn>();
        for (IntrospectedColumn ic : columns) {
            if (!ic.isGeneratedAlways() && !ic.isIdentity()) {
                filteredList.add(ic);
            }
        }
        return filteredList;
    }
}
