package com.liuyun.builder.internal;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.liuyun.builder.api.CommentGenerator;
import com.liuyun.builder.api.FullyQualifiedTable;
import com.liuyun.builder.api.IntrospectedColumn;
import com.liuyun.builder.api.IntrospectedTable;
import com.liuyun.builder.api.JavaFormatter;
import com.liuyun.builder.api.JavaTypeResolver;
import com.liuyun.builder.api.Plugin;
import com.liuyun.builder.api.XmlFormatter;
import com.liuyun.builder.api.dom.DefaultJavaFormatter;
import com.liuyun.builder.api.dom.DefaultXmlFormatter;
import com.liuyun.builder.codegen.core.IntrospectedTableImpl;
import com.liuyun.builder.config.PropertyRegistry;
import com.liuyun.builder.config.label.CommentGeneratorConfiguration;
import com.liuyun.builder.config.label.Context;
import com.liuyun.builder.config.label.JavaTypeResolverConfiguration;
import com.liuyun.builder.config.label.PluginConfiguration;
import com.liuyun.builder.config.label.TablesConfiguration;
import com.liuyun.builder.internal.types.JavaTypeResolverDefaultImpl;

//对象生成工厂
public class ObjectFactory {

    private static List<ClassLoader> externalClassLoaders;

    static {
        externalClassLoaders = new ArrayList<ClassLoader>();
    }

    private ObjectFactory() {
        super();
    }

    public static void reset() {
        externalClassLoaders.clear();
    }

    //添加额外的加载器
    public static synchronized void addExternalClassLoader(ClassLoader classLoader) {
        ObjectFactory.externalClassLoaders.add(classLoader);
    }
    
    //根据名称加载外部类型
    public static Class<?> externalClassForName(String type) throws ClassNotFoundException {
        Class<?> clazz;
        for (ClassLoader classLoader : externalClassLoaders) {
            try {
                clazz = Class.forName(type, true, classLoader);
                return clazz;
            } catch (Throwable e) {
                // ignore
            }
        }
        return internalClassForName(type);
    }

    //创建额外的对象
    public static Object createExternalObject(String type) {
        Object answer;
        try {
            Class<?> clazz = externalClassForName(type);
            answer = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(getString("RuntimeError.6", type), e); 
        }
        return answer;
    }

    //根据名称加载内部类型
    public static Class<?> internalClassForName(String type) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            clazz = Class.forName(type, true, cl);
        } catch (Exception e) {
            // ignore
        }
        if (clazz == null) {
            clazz = Class.forName(type, true, ObjectFactory.class.getClassLoader());
        }
        return clazz;
    }

    //获取资源链接
    public static URL getResource(String resource) {
        URL url;
        for (ClassLoader classLoader : externalClassLoaders) {
            url = classLoader.getResource(resource);
            if (url != null) {
                return url;
            }
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        url = cl.getResource(resource);
        if (url == null) {
            url = ObjectFactory.class.getClassLoader().getResource(resource);
        }
        return url;
    }

    //根据类型名创建内部对象
    public static Object createInternalObject(String type) {
        Object answer;
        try {
            Class<?> clazz = internalClassForName(type);
            answer = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(getString("RuntimeError.6", type), e); 
        }
        return answer;
    }

    //创建Java类型转换器
    public static JavaTypeResolver createJavaTypeResolver(Context context, List<String> warnings) {
        JavaTypeResolverConfiguration config = context.getJavaTypeResolverConfiguration();
        String type;
        if (config != null && config.getConfigurationType() != null) {
            type = config.getConfigurationType();
            if ("DEFAULT".equalsIgnoreCase(type)) { 
                type = JavaTypeResolverDefaultImpl.class.getName();
            }
        } else {
            type = JavaTypeResolverDefaultImpl.class.getName();
        }
        JavaTypeResolver answer = (JavaTypeResolver) createInternalObject(type);
        answer.setWarnings(warnings);
        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }
        answer.setContext(context);
        return answer;
    }

    //创建插件
    public static Plugin createPlugin(Context context, PluginConfiguration pluginConfiguration) {
        Plugin plugin = (Plugin) createInternalObject(pluginConfiguration.getConfigurationType());
        plugin.setContext(context);
        plugin.setProperties(pluginConfiguration.getProperties());
        return plugin;
    }

    //创建Java格式器
    public static JavaFormatter createJavaFormatter(Context context) {
        String type = context.getProperty(PropertyRegistry.CONTEXT_JAVA_FORMATTER);
        if (!stringHasValue(type)) {
            type = DefaultJavaFormatter.class.getName();
        }
        JavaFormatter answer = (JavaFormatter) createInternalObject(type);
        answer.setContext(context);
        return answer;
    }

    //创建XML格式器
    public static XmlFormatter createXmlFormatter(Context context) {
        String type = context.getProperty(PropertyRegistry.CONTEXT_XML_FORMATTER);
        if (!stringHasValue(type)) {
            type = DefaultXmlFormatter.class.getName();
        }
        XmlFormatter answer = (XmlFormatter) createInternalObject(type);
        answer.setContext(context);
        return answer;
    }

    //创建逆向表
    public static IntrospectedTable createIntrospectedTable(TablesConfiguration tableConfiguration, 
    		FullyQualifiedTable table, Context context) {
    	//创建IntrospectedTable对象
        IntrospectedTable answer = createIntrospectedTableForValidation(context);
        answer.setFullyQualifiedTable(table);
        answer.setTableConfiguration(tableConfiguration);
        return answer;
    }
    
    //创建IntrospectedTable对象
    public static IntrospectedTable createIntrospectedTableForValidation(Context context) {
        //根据类型名创建IntrospectedTable对象
        IntrospectedTable answer = (IntrospectedTable) createInternalObject(IntrospectedTableImpl.class.getName());
        answer.setContext(context);
        return answer;
    }

    //创建逆向列
    public static IntrospectedColumn createIntrospectedColumn(Context context) {
        IntrospectedColumn answer = (IntrospectedColumn) createInternalObject(IntrospectedColumn.class.getName());
        answer.setContext(context);
        return answer;
    }
    
    //创建注释生成器
    public static CommentGenerator createCommentGenerator(Context context) {
    	//获取注释生成器配置
        CommentGeneratorConfiguration config = context.getCommentGeneratorConfiguration();
        CommentGenerator answer;
        String type;
        if (config == null || config.getConfigurationType() == null) {
        	//如果为空则用默认注释生成器
            type = NullCommentGenerator.class.getName();
        } else {
        	//否则使用配置的注释生成器
            type = config.getConfigurationType();
        }
        answer = (CommentGenerator) createInternalObject(type);
        if (config != null) {
            answer.addConfigurationProperties(config.getProperties());
        }
        return answer;
    }
    
}
