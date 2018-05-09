package com.liuyun.builder.config.xml;

import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import com.liuyun.builder.codegen.util.XmlConstants;
import com.liuyun.builder.config.Configuration;
import com.liuyun.builder.exception.XMLParserException;

//配置解析器
public class ConfigurationParser {

    private List<String> warnings;
    private List<String> parseErrors;
    private Properties extraProperties;

    //构造器
    public ConfigurationParser(List<String> warnings) {
        this(null, warnings);
    }
    
    //构造器
    public ConfigurationParser(Properties extraProperties, List<String> warnings) {
        super();
        this.extraProperties = extraProperties;
        if (warnings == null) {
            this.warnings = new ArrayList<String>();
        } else {
            this.warnings = warnings;
        }
        parseErrors = new ArrayList<String>();
    }

    //根据传入的File解析
    public Configuration parseConfiguration(File inputFile) throws IOException, XMLParserException {
        FileReader fr = new FileReader(inputFile);
        return parseConfiguration(fr);
    }

    //根据传入的Reader流解析
    public Configuration parseConfiguration(Reader reader) throws IOException, XMLParserException {
        InputSource is = new InputSource(reader);
        return parseConfiguration(is);
    }

    //根据传入的InputStream解析
    public Configuration parseConfiguration(InputStream inputStream) throws IOException, XMLParserException {
        InputSource is = new InputSource(inputStream);
        return parseConfiguration(is);
    }

    //根据输入流解析配置文件
    private Configuration parseConfiguration(InputSource inputSource) throws IOException, XMLParserException {
        parseErrors.clear();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        try {
        	//获取DocumentBuilder对象
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new ParserEntityResolver());
            //获取ParserErrorHandler对象
            ParserErrorHandler handler = new ParserErrorHandler(warnings, parseErrors);
            builder.setErrorHandler(handler);
            Document document = null;
            try {
                document = builder.parse(inputSource);
            } catch (SAXParseException e) {
                throw new XMLParserException(parseErrors);
            } catch (SAXException e) {
                if (e.getException() == null) {
                    parseErrors.add(e.getMessage());
                } else {
                    parseErrors.add(e.getException().getMessage());
                }
            }
            if (parseErrors.size() > 0) {
                throw new XMLParserException(parseErrors);
            }
            Configuration config;
            //获取根结点
            Element rootNode = document.getDocumentElement();
            //获取Document类型
            DocumentType docType = document.getDoctype();
            //根据DOCTYPE决定使用哪个解析
            if (rootNode.getNodeType() == Node.ELEMENT_NODE 
            		&& docType.getPublicId().equals(XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID)) {
            	config = parseConfiguration(rootNode);
            } else {
                throw new XMLParserException(getString("RuntimeError.5")); 
            }
            if (parseErrors.size() > 0) {
                throw new XMLParserException(parseErrors);
            }
            return config;
        } catch (ParserConfigurationException e) {
            parseErrors.add(e.getMessage());
            throw new XMLParserException(parseErrors);
        }
    }

    //解析配置方法
    private Configuration parseConfiguration(Element rootNode) throws XMLParserException {
    	//新建MyBatisGenerator配置解析器
        MyBatisGeneratorConfigurationParser parser = new MyBatisGeneratorConfigurationParser(extraProperties);
        //调用解析方法从根结点开始解析
        return parser.parseConfiguration(rootNode);
    }
}
