package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.liuyun.builder.api.MyBatisGenerator;
import com.liuyun.builder.config.Configuration;
import com.liuyun.builder.config.xml.ConfigurationParser;
import com.liuyun.builder.internal.DefaultShellCallback;

public class MyBatisGeneratorTest {

    @Test
    public void testConfig() throws Exception {
    	
        List<String> warnings = new ArrayList<String>();
        
        ConfigurationParser cp = new ConfigurationParser(warnings);
        
        Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream("config.xml"));
        
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
        
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
        
        myBatisGenerator.generate(null, null, null, true);
        
    }
    
}
