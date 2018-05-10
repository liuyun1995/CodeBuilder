package com.liuyun.builder.api;

import static com.liuyun.builder.internal.utils.ClassloaderUtil.getCustomClassloader;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.liuyun.builder.codegen.core.RootClassInfo;
import com.liuyun.builder.config.Configuration;
import com.liuyun.builder.config.MergeConstants;
import com.liuyun.builder.config.label.Context;
import com.liuyun.builder.exception.InvalidConfigurationException;
import com.liuyun.builder.exception.ShellException;
import com.liuyun.builder.internal.DefaultShellCallback;
import com.liuyun.builder.internal.NullProgressCallback;
import com.liuyun.builder.internal.ObjectFactory;
import com.liuyun.builder.internal.XmlFileMergerJaxp;

public class MyBatisGenerator {

    //配置类
    private Configuration configuration;
    
    //shell回调器
    private ShellCallback shellCallback;

    //java文件集合
    private List<GeneratedJavaFile> generatedJavaFiles;

    //xml文件集合
    private List<GeneratedXmlFile> generatedXmlFiles;

    //警告信息
    private List<String> warnings;
    
    //projects集合
    private Set<String> projects;

    public MyBatisGenerator(Configuration configuration, ShellCallback shellCallback,
            List<String> warnings) throws InvalidConfigurationException {
        super();
        if (configuration == null) {
            throw new IllegalArgumentException(getString("RuntimeError.2")); 
        } else {
            this.configuration = configuration;
        }
        if (shellCallback == null) {
            this.shellCallback = new DefaultShellCallback(false);
        } else {
            this.shellCallback = shellCallback;
        }
        if (warnings == null) {
            this.warnings = new ArrayList<String>();
        } else {
            this.warnings = warnings;
        }
        generatedJavaFiles = new ArrayList<GeneratedJavaFile>();
        generatedXmlFiles = new ArrayList<GeneratedXmlFile>();
        projects = new HashSet<String>();
        //对配置进行验证
        this.configuration.validate();
    }

    public void generate(ProgressCallback callback) 
    		throws SQLException, IOException, InterruptedException {
        generate(callback, null, null, true);
    }

    public void generate(ProgressCallback callback, Set<String> contextIds) 
    		throws SQLException, IOException, InterruptedException {
        generate(callback, contextIds, null, true);
    }

    //生成代码的入口方法
    public void generate(ProgressCallback callback, Set<String> contextIds, Set<String> fullyQualifiedTableNames) 
    		throws SQLException, IOException, InterruptedException {
        generate(callback, contextIds, fullyQualifiedTableNames, true);
    }

    //核心生成方法
    public void generate(ProgressCallback callback, Set<String> contextIds,
            Set<String> fullyQualifiedTableNames, boolean writeFiles) throws SQLException,
            IOException, InterruptedException {
        if (callback == null) {
            callback = new NullProgressCallback();
        }
        //重置操作
        generatedJavaFiles.clear();
        generatedXmlFiles.clear();
        ObjectFactory.reset();
        RootClassInfo.reset();
        
        List<Context> contextsToRun;
        //只运行命令行指定的context, 若命令行未指定则运行配置声明的context
        if (contextIds == null || contextIds.size() == 0) {
            contextsToRun = configuration.getContexts();
        } else {
            contextsToRun = new ArrayList<Context>();
            for (Context context : configuration.getContexts()) {
                if (contextIds.contains(context.getId())) {
                    contextsToRun.add(context);
                }
            }
        }
        
        //加载ClassPathEntries配置的数据库驱动
        if (configuration.getClassPathEntries().size() > 0) {
            ClassLoader classLoader = getCustomClassloader(configuration.getClassPathEntries());
            ObjectFactory.addExternalClassLoader(classLoader);
        }
        
        int totalSteps = 0;
        for (Context context : contextsToRun) {
            totalSteps += context.getIntrospectionSteps();
        }
        //设置回调器状态为：开始逆向转换
        callback.introspectionStarted(totalSteps);
        //对所有的context进行逆向转换
        for (Context context : contextsToRun) {
            context.introspectTables(callback, warnings, fullyQualifiedTableNames);
        }
        
        totalSteps = 0;
        for (Context context : contextsToRun) {
            totalSteps += context.getGenerationSteps();
        }
        //设置回调器状态为：开始生成文件
        callback.generationStarted(totalSteps);
        //收集每个context要生成的文件
        for (Context context : contextsToRun) {
            context.generateFiles(callback, generatedJavaFiles, generatedXmlFiles, warnings);
        }
        //下面开始写入文件
        if (writeFiles) {
            callback.saveStarted(generatedXmlFiles.size() + generatedJavaFiles.size());
            //遍历要写的xml文件
            for (GeneratedXmlFile gxf : generatedXmlFiles) {
                projects.add(gxf.getTargetProject());
                writeGeneratedXmlFile(gxf, callback);
            }
            //遍历要写的java文件
            for (GeneratedJavaFile gjf : generatedJavaFiles) {
                projects.add(gjf.getTargetProject());
                writeGeneratedJavaFile(gjf, callback);
            }
            //对每个工程进行刷新
            for (String project : projects) {
                shellCallback.refreshProject(project);
            }
        }
        callback.done();
    }

    //生成Java文件
    private void writeGeneratedJavaFile(GeneratedJavaFile gjf, ProgressCallback callback)
            throws InterruptedException, IOException {
        File targetFile;
        String source;
        try {
            File directory = shellCallback.getDirectory(gjf.getTargetProject(), gjf.getTargetPackage());
            targetFile = new File(directory, gjf.getFileName());
            //如果目标文件已经存在
            if (targetFile.exists()) {
            	//是否要合并
                if (shellCallback.isMergeSupported()) {
                    source = shellCallback.mergeJavaFile(gjf.getFormattedContent(), targetFile,
                            MergeConstants.OLD_ELEMENT_TAGS,
                            gjf.getFileEncoding());
                //是否要覆盖
                } else if (shellCallback.isOverwriteEnabled()) {
                    source = gjf.getFormattedContent();
                    warnings.add(getString("Warning.11", targetFile.getAbsolutePath()));
                //否则就换一个名字
                } else {
                    source = gjf.getFormattedContent();
                    targetFile = getUniqueFileName(directory, gjf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.getAbsolutePath())); 
                }
            } else {
                source = gjf.getFormattedContent();
            }
            callback.checkCancel();
            callback.startTask(getString("Progress.15", targetFile.getName()));
            //将字符串写入目标文件
            writeFile(targetFile, source, gjf.getFileEncoding());
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    //生成XML文件
    private void writeGeneratedXmlFile(GeneratedXmlFile gxf, ProgressCallback callback)
            throws InterruptedException, IOException {
        File targetFile;
        String source;
        try {
        	//获取目录
            File directory = shellCallback.getDirectory(gxf.getTargetProject(), gxf.getTargetPackage());
            //生成目标文件
            targetFile = new File(directory, gxf.getFileName());
            //如果目标文件已存在
            if (targetFile.exists()) {
            	//如果要合并就合并文件
                if (gxf.isMergeable()) {
                    source = XmlFileMergerJaxp.getMergedSource(gxf, targetFile);
                //否则判断是否要覆盖
                } else if (shellCallback.isOverwriteEnabled()) {
                    source = gxf.getFormattedContent();
                    warnings.add(getString("Warning.11", targetFile.getAbsolutePath()));
                //否则就换一个名字
                } else {
                    source = gxf.getFormattedContent();
                    targetFile = getUniqueFileName(directory, gxf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.getAbsolutePath())); 
                }
            } else {
                source = gxf.getFormattedContent();
            }
            callback.checkCancel();
            callback.startTask(getString("Progress.15", targetFile.getName())); 
            //将字符串写入目标文件
            writeFile(targetFile, source, "UTF-8"); 
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    //将字符串写入文件
    private void writeFile(File file, String content, String fileEncoding) throws IOException {
        FileOutputStream fos = new FileOutputStream(file, false);
        OutputStreamWriter osw;
        if (fileEncoding == null) {
            osw = new OutputStreamWriter(fos);
        } else {
            osw = new OutputStreamWriter(fos, fileEncoding);
        }
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(content);
        bw.close();
    }

    //获取唯一的文件名
    private File getUniqueFileName(File directory, String fileName) {
        File answer = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 1000; i++) {
            sb.setLength(0);
            sb.append(fileName);
            sb.append('.');
            sb.append(i);

            File testFile = new File(directory, sb.toString());
            if (!testFile.exists()) {
                answer = testFile;
                break;
            }
        }
        if (answer == null) {
            throw new RuntimeException(getString("RuntimeError.3", directory.getAbsolutePath())); 
        }
        return answer;
    }

    //获取要生成的Java文件列表
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        return generatedJavaFiles;
    }

    //获取要生成的XML文件列表
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        return generatedXmlFiles;
    }
    
}
