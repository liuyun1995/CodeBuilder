package com.liuyun.builder.config;

//属性注册器
public class PropertyRegistry {

    //分辨于表和Java模型生成器
    public static final String ANY_ROOT_CLASS = "rootClass";

    //分辨于表和JavaClient生成器
    public static final String ANY_ROOT_INTERFACE = "rootInterface"; 

    public static final String CONTEXT_BEGINNING_DELIMITER = "beginningDelimiter"; 
    public static final String CONTEXT_ENDING_DELIMITER = "endingDelimiter"; 
    public static final String CONTEXT_AUTO_DELIMIT_KEYWORDS = "autoDelimitKeywords"; 
    public static final String CONTEXT_JAVA_FILE_ENCODING = "javaFileEncoding"; 
    public static final String CONTEXT_JAVA_FORMATTER = "javaFormatter"; 
    public static final String CONTEXT_XML_FORMATTER = "xmlFormatter"; 

    public static final String CLIENT_USE_LEGACY_BUILDER = "useLegacyBuilder"; 
    
    public static final String DAO_EXAMPLE_METHOD_VISIBILITY = "exampleMethodVisibility"; 
    public static final String DAO_METHOD_NAME_CALCULATOR = "methodNameCalculator"; 

    public static final String TYPE_RESOLVER_FORCE_BIG_DECIMALS = "forceBigDecimals"; 
    public static final String TYPE_RESOLVER_USE_JSR310_TYPES = "useJSR310Types"; 

    public static final String COMMENT_GENERATOR_SUPPRESS_DATE = "suppressDate"; 
    public static final String COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS = "suppressAllComments"; 
    public static final String COMMENT_GENERATOR_ADD_REMARK_COMMENTS = "addRemarkComments"; 
    public static final String COMMENT_GENERATOR_DATE_FORMAT = "dateFormat";

    //构造FullyQualifiedTable时使用
	public static final String TABLE_RUNTIME_CATALOG = null;
	public static final String TABLE_RUNTIME_SCHEMA = null;
	public static final String TABLE_RUNTIME_TABLE_NAME = null;
	public static final String TABLE_IGNORE_QUALIFIERS_AT_RUNTIME = null; 
    
}
