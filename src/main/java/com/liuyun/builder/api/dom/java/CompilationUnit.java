package com.liuyun.builder.api.dom.java;

import java.util.List;
import java.util.Set;

//编译单元
public interface CompilationUnit {

	//获取格式化内容
    String getFormattedContent();

    //获取导入类型集合
    Set<FullyQualifiedJavaType> getImportedTypes();

    //获取静态导入类型集合
    Set<String> getStaticImports();

    //获取父类类型
    FullyQualifiedJavaType getSuperClass();

    //是否是接口类
    boolean isJavaInterface();

    //是否是枚举类
    boolean isJavaEnumeration();

    //获取接口集合
    Set<FullyQualifiedJavaType> getSuperInterfaceTypes();

    //获取编译单元类型
    FullyQualifiedJavaType getType();

    //添加导入类型
    void addImportedType(FullyQualifiedJavaType importedType);

    //添加导入类型
    void addImportedTypes(Set<FullyQualifiedJavaType> importedTypes);

    //添加静态导入类型
    void addStaticImport(String staticImport);

    //添加静态导入类型
    void addStaticImports(Set<String> staticImports);
    
    //添加文件注释行
    void addFileCommentLine(String commentLine);

    //添加文件注释行
    List<String> getFileCommentLines();
    
}
