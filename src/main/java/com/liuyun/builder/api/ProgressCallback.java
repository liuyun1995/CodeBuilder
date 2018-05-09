package com.liuyun.builder.api;

//进程回调
public interface ProgressCallback {
    
    void introspectionStarted(int totalTasks);
    
    void generationStarted(int totalTasks);
    
    void saveStarted(int totalTasks);
    
    void startTask(String taskName);
    
    void done();
    
    void checkCancel() throws InterruptedException;
    
}
