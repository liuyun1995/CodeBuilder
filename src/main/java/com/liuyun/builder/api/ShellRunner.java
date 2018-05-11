package com.liuyun.builder.api;

import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import com.liuyun.builder.config.Configuration;
import com.liuyun.builder.config.xml.ConfigurationParser;
import com.liuyun.builder.exception.InvalidConfigurationException;
import com.liuyun.builder.exception.XMLParserException;
import com.liuyun.builder.internal.DefaultShellCallback;
import com.liuyun.builder.logging.LogFactory;

//命令行入口类
public class ShellRunner {
	
    private static final String CONFIG_FILE = "-configfile"; 
    private static final String OVERWRITE = "-overwrite"; 
    private static final String CONTEXT_IDS = "-contextids"; 
    private static final String TABLES = "-tables"; 
    private static final String VERBOSE = "-verbose";
    private static final String FORCE_JAVA_LOGGING = "-forceJavaLogging";
    private static final String HELP_1 = "-?";
    private static final String HELP_2 = "-h";

    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            System.exit(0);
            return;
        }

        Map<String, String> arguments = parseCommandLine(args);

        //打印用例信息
        if (arguments.containsKey(HELP_1)) {
            usage();
            System.exit(0);
            return;
        }

        //如果没指定配置文件则报错
        if (!arguments.containsKey(CONFIG_FILE)) {
            writeLine(getString("RuntimeError.0")); 
            return;
        }

        List<String> warnings = new ArrayList<String>();

        //获取配置文件路径
        String configfile = arguments.get(CONFIG_FILE);
        File configurationFile = new File(configfile);
        if (!configurationFile.exists()) {
            writeLine(getString("RuntimeError.1", configfile)); 
            return;
        }

        Set<String> fullyqualifiedTables = new HashSet<String>();
        //设置需要逆向转换的table
        if (arguments.containsKey(TABLES)) {
            StringTokenizer st = new StringTokenizer(arguments.get(TABLES), ","); 
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }

        Set<String> contexts = new HashSet<String>();
        //设置需要逆向转换的context
        if (arguments.containsKey(CONTEXT_IDS)) {
            StringTokenizer st = new StringTokenizer(arguments.get(CONTEXT_IDS), ","); 
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    contexts.add(s);
                }
            }
        }

        try {
        	//获取属性解析器
            ConfigurationParser cp = new ConfigurationParser(warnings);
            //解析配置文件
            Configuration config = cp.parseConfiguration(configurationFile);
            //获取shell回调器
            DefaultShellCallback shellCallback = new DefaultShellCallback(arguments.containsKey(OVERWRITE));
            //构造MyBatisGenerator
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            //获取进程回调器
            ProgressCallback progressCallback = arguments.containsKey(VERBOSE) ? new VerboseProgressCallback() : null;
            //调用生成文件的方法
            myBatisGenerator.generate(progressCallback, contexts, fullyqualifiedTables);
        } catch (XMLParserException e) {
            writeLine(getString("Progress.3")); 
            writeLine();
            for (String error : e.getErrors()) {
                writeLine(error);
            }
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (InvalidConfigurationException e) {
            writeLine(getString("Progress.16")); 
            for (String error : e.getErrors()) {
                writeLine(error);
            }
            return;
        } catch (InterruptedException e) {
            // ignore (will never happen with the DefaultShellCallback)
        }
        
        //在控制台打印警告信息(由于warnings初始化了, 不为null, 所以不会报错)
        for (String warning : warnings) {
            writeLine(warning);
        }
        if (warnings.size() == 0) {
            writeLine(getString("Progress.4")); 
        } else {
            writeLine();
            writeLine(getString("Progress.5")); 
        }
    }

    private static void usage() {
        String lines = getString("Usage.Lines"); 
        int intLines = Integer.parseInt(lines);
        for (int i = 0; i < intLines; i++) {
            String key = "Usage." + i; 
            writeLine(getString(key));
        }
    }

    private static void writeLine(String message) {
        System.out.println(message);
    }

    private static void writeLine() {
        System.out.println();
    }

    //解析命令行参数
    private static Map<String, String> parseCommandLine(String[] args) {
    	//新建错误列表
        List<String> errors = new ArrayList<String>();
        //新建参数映射表
        Map<String, String> arguments = new HashMap<String, String>();
        //遍历参数数组
        for (int i = 0; i < args.length; i++) {
            if (CONFIG_FILE.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(CONFIG_FILE, args[i + 1]);
                } else {
                    errors.add(getString("RuntimeError.19", CONFIG_FILE)); 
                }
                i++;
            } else if (OVERWRITE.equalsIgnoreCase(args[i])) {
                arguments.put(OVERWRITE, "Y"); 
            } else if (VERBOSE.equalsIgnoreCase(args[i])) {
                arguments.put(VERBOSE, "Y"); 
            } else if (HELP_1.equalsIgnoreCase(args[i])) {
                arguments.put(HELP_1, "Y"); 
            } else if (HELP_2.equalsIgnoreCase(args[i])) {
                // put HELP_1 in the map here too - so we only
                // have to check for one entry in the mainline
                arguments.put(HELP_1, "Y"); 
            } else if (FORCE_JAVA_LOGGING.equalsIgnoreCase(args[i])) {
            	//用于指定日志生成工厂
                LogFactory.forceJavaLogging();
            } else if (CONTEXT_IDS.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(CONTEXT_IDS, args[i + 1]);
                } else {
                    errors.add(getString("RuntimeError.19", CONTEXT_IDS)); 
                }
                i++;
            } else if (TABLES.equalsIgnoreCase(args[i])) {
                if ((i + 1) < args.length) {
                    arguments.put(TABLES, args[i + 1]);
                } else {
                    errors.add(getString("RuntimeError.19", TABLES)); 
                }
                i++;
            } else {
                errors.add(getString("RuntimeError.20", args[i])); 
            }
        }
        if (!errors.isEmpty()) {
            for (String error : errors) {
                writeLine(error);
            }
            System.exit(-1);
        }
        return arguments;
    }
}
