package com.liuyun.builder.api;

//抽象文件生成器
public abstract class GeneratedFile {
	
	protected String targetProject;  //项目路径
    protected String targetPackage;  //包路径

    public GeneratedFile(String targetProject, String targetPackage) {
        super();
        this.targetProject = targetProject;
        this.targetPackage = targetPackage;
    }

    //获取格式化文本
    public abstract String getFormattedContent();
    
    //获取文件名
    public abstract String getFileName();
    
    //获取项目路径
    public abstract String getTargetProject();

    //获取包路径
    public abstract String getTargetPackage();
    
    //是否可以合并
    public abstract boolean isMergeable();

	@Override
    public String toString() {
        return getFormattedContent();
    }
    
}
