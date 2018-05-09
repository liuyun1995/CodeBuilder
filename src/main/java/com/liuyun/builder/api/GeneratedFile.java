package com.liuyun.builder.api;

//抽象文件生成器
public abstract class GeneratedFile {

    protected String target;

    public GeneratedFile(String target) {
        super();
        this.target = target;
    }

    //获取格式化文本
    public abstract String getFormattedContent();
    
    //获取文件名
    public abstract String getFileName();

    //获取目标路径
    public abstract String getTargetPackage();
    
    //是否可以合并
    public abstract boolean isMergeable();
    
    public String getTarget() {
		return target;
	}

	@Override
    public String toString() {
        return getFormattedContent();
    }
    
}
