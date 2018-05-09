package com.liuyun.builder.api;

import java.io.File;

import com.liuyun.builder.exception.ShellException;

//Shell回调
public interface ShellCallback {

	//获取目录
    File getDirectory(String targetProject, String targetPackage) throws ShellException;

    //合并文件
    String mergeJavaFile(String newFileSource, File existingFile, String[] javadocTags, String fileEncoding) throws ShellException;

    //刷新项目
    void refreshProject(String project);

    //是否支持合并
    boolean isMergeSupported();

    //是否需要覆盖
    boolean isOverwriteEnabled();
    
}
