package com.liuyun.builder.api.dom.java;

import static com.liuyun.builder.api.dom.OutputUtil.calculateImports;
import static com.liuyun.builder.api.dom.OutputUtil.newLine;
import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

//普通类头部
public class TopLevelClass extends InnerClass implements CompilationUnit {

	//导入类型
    private Set<FullyQualifiedJavaType> importedTypes;

    //静态导入
    private Set<String> staticImports;

    //文件注释
    private List<String> fileCommentLines;

    public TopLevelClass(FullyQualifiedJavaType type) {
        super(type);
        importedTypes = new TreeSet<FullyQualifiedJavaType>();
        fileCommentLines = new ArrayList<String>();
        staticImports = new TreeSet<String>();
    }

    public TopLevelClass(String typeName) {
        this(new FullyQualifiedJavaType(typeName));
    }

    //获取导入类型
    @Override
    public Set<FullyQualifiedJavaType> getImportedTypes() {
        return importedTypes;
    }

    //添加导入类型
    public void addImportedType(String importedType) {
        addImportedType(new FullyQualifiedJavaType(importedType));
    }

    //添加导入类型
    @Override
    public void addImportedType(FullyQualifiedJavaType importedType) {
        if (importedType != null
                && importedType.isExplicitlyImported()
                && !importedType.getPackageName().equals(getType().getPackageName())
                && !importedType.getShortName().equals(getType().getShortName())) {
            importedTypes.add(importedType);
        }
    }

    //获取格式化内容
    @Override
    public String getFormattedContent() {
        StringBuilder sb = new StringBuilder();

        for (String fileCommentLine : fileCommentLines) {
            sb.append(fileCommentLine);
            newLine(sb);
        }

        if (stringHasValue(getType().getPackageName())) {
            sb.append("package ");
            sb.append(getType().getPackageName());
            sb.append(';');
            newLine(sb);
            newLine(sb);
        }

        for (String staticImport : staticImports) {
            sb.append("import static ");
            sb.append(staticImport);
            sb.append(';');
            newLine(sb);
        }

        if (staticImports.size() > 0) {
            newLine(sb);
        }

        Set<String> importStrings = calculateImports(importedTypes);
        for (String importString : importStrings) {
            sb.append(importString);
            newLine(sb);
        }

        if (importStrings.size() > 0) {
            newLine(sb);
        }

        sb.append(super.getFormattedContent(0, this));

        return sb.toString();
    }

    //是否是接口类型
    @Override
    public boolean isJavaInterface() {
        return false;
    }

    //是否是枚举类型
    @Override
    public boolean isJavaEnumeration() {
        return false;
    }

    //添加文件注释行
    @Override
    public void addFileCommentLine(String commentLine) {
        fileCommentLines.add(commentLine);
    }

    //获取文件注释行
    @Override
    public List<String> getFileCommentLines() {
        return fileCommentLines;
    }

    //添加导入类型
    @Override
    public void addImportedTypes(Set<FullyQualifiedJavaType> importedTypes) {
        this.importedTypes.addAll(importedTypes);
    }

    //获取静态导入
    @Override
    public Set<String> getStaticImports() {
        return staticImports;
    }

    //添加静态导入
    @Override
    public void addStaticImport(String staticImport) {
        staticImports.add(staticImport);
    }

    //添加静态导入
    @Override
    public void addStaticImports(Set<String> staticImports) {
        this.staticImports.addAll(staticImports);
    }
}
