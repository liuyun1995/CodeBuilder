package com.liuyun.builder.api;

import org.mybatis.generator.internal.NullProgressCallback;

public class VerboseProgressCallback extends NullProgressCallback {

    public VerboseProgressCallback() {
        super();
    }

    @Override
    public void startTask(String taskName) {
        System.out.println(taskName);
    }
    
}
