package com.liuyun.builder.internal;

import static com.liuyun.builder.internal.utils.messages.Messages.getString;

import java.io.File;
import java.util.StringTokenizer;

import com.liuyun.builder.api.ShellCallback;
import com.liuyun.builder.exception.ShellException;

//默认的shell回调
public class DefaultShellCallback implements ShellCallback {

    private boolean overwrite;

    public DefaultShellCallback(boolean overwrite) {
        super();
        this.overwrite = overwrite;
    }
    
    @Override
    public File getDirectory(String targetProject, String targetPackage) throws ShellException {
        File project = new File(targetProject);
        if (!project.isDirectory()) {
            throw new ShellException(getString("Warning.9", targetProject));
        }
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, "."); 
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }
        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new ShellException(getString("Warning.10", directory.getAbsolutePath()));
            }
        }
        return directory;
    }

    @Override
    public void refreshProject(String project) {}

    @Override
    public boolean isMergeSupported() {
        return false;
    }

    @Override
    public boolean isOverwriteEnabled() {
        return overwrite;
    }

    @Override
    public String mergeJavaFile(String newFileSource,
            File existingFile, String[] javadocTags, String fileEncoding)
            throws ShellException {
        throw new UnsupportedOperationException();
    }
    
}
