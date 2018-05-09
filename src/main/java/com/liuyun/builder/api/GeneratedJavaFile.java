package com.liuyun.builder.api;

import com.liuyun.builder.api.dom.java.CompilationUnit;

//Java文件生成器
public class GeneratedJavaFile extends GeneratedFile {

    //编译单元
    private CompilationUnit compilationUnit;

    //文件编码
    private String fileEncoding;

    //格式化器
    private JavaFormatter javaFormatter;

    public GeneratedJavaFile(CompilationUnit compilationUnit,
            String target,
            String fileEncoding,
            JavaFormatter javaFormatter) {
        super(target);
        this.compilationUnit = compilationUnit;
        this.fileEncoding = fileEncoding;
        this.javaFormatter = javaFormatter;
    }

    public GeneratedJavaFile(CompilationUnit compilationUnit,
            String targetProject,
            JavaFormatter javaFormatter) {
        this(compilationUnit, targetProject, null, javaFormatter);
    }
    
    //获取格式化内容
    @Override
    public String getFormattedContent() {
        return javaFormatter.getFormattedContent(compilationUnit);
    }
    
    //获取文件名
    @Override
    public String getFileName() {
        return compilationUnit.getType().getShortNameWithoutTypeArguments() + ".java";
    }

    //获取目标路径
    @Override
    public String getTargetPackage() {
        return compilationUnit.getType().getPackageName();
    }
    
    //获取编译单元
    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }
    
    //是否可以合并
    @Override
    public boolean isMergeable() {
        return true;
    }

    //获取文件编码
    public String getFileEncoding() {
        return fileEncoding;
    }
    
}
