package com.liuyun.builder.internal.utils;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassloaderUtil {

    private ClassloaderUtil() {}

    public static ClassLoader getCustomClassloader(Collection<String> entries) {
        List<URL> urls = new ArrayList<URL>();
        File file;
        if (entries != null) {
            for (String classPathEntry : entries) {
                file = new File(classPathEntry);
                if (!file.exists()) {
                    throw new RuntimeException(getString("RuntimeError.9", classPathEntry)); 
                }
                try {
                    urls.add(file.toURI().toURL());
                } catch (MalformedURLException e) {
                    // this shouldn't happen, but just in case...
                    throw new RuntimeException(getString("RuntimeError.9", classPathEntry)); 
                }
            }
        }
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[urls.size()]), parent);
        return ucl;
    }
}
