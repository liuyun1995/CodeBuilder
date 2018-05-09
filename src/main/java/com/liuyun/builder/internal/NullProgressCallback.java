package com.liuyun.builder.internal;

import com.liuyun.builder.api.ProgressCallback;

public class NullProgressCallback implements ProgressCallback {

    public NullProgressCallback() {
        super();
    }

    @Override
    public void generationStarted(int totalTasks) {}

    @Override
    public void introspectionStarted(int totalTasks) {}

    @Override
    public void saveStarted(int totalTasks) {}

    @Override
    public void startTask(String taskName) {}

    @Override
    public void checkCancel() throws InterruptedException {}

    @Override
    public void done() {}
    
}
