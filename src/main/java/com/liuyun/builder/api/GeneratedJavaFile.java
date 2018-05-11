package com.liuyun.builder.api;

import com.liuyun.builder.api.dom.java.CompilationUnit;

//Java文件生成器(策略模式)
public class GeneratedJavaFile extends GeneratedFile {
	
    private CompilationUnit compilationUnit;    //编译单元
    private String fileEncoding;                //文件编码
    private JavaFormatter javaFormatter;        //格式化器

    public GeneratedJavaFile(CompilationUnit compilationUnit,
            String targetProject,
            String targetPackage,
            String fileEncoding,
            JavaFormatter javaFormatter) {
        super(targetProject, targetPackage);
        this.compilationUnit = compilationUnit;
        this.fileEncoding = fileEncoding;
        this.javaFormatter = javaFormatter;
    }
    
    //获取格式化内容
    @Override
    public String getFormattedContent() {
        return javaFormatter.getFormattedContent(compilationUnit);
    }
    
    //获取文件名
    @Override
    public String getFileName() {
    	//根据编译单元生成文件名, 因为java类名需要和文件名相同
        return compilationUnit.getType().getShortNameWithoutTypeArguments() + ".java";
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
        return true;
    }
    
    //获取编译单元
    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    //获取文件编码
    public String getFileEncoding() {
        return fileEncoding;
    }
    
}
