package com.liuyun.builder.config;

//合并常量
public class MergeConstants {

    private MergeConstants() {}

    public static final String[] OLD_XML_ELEMENT_PREFIXES = {"ibatorgenerated_", "abatorgenerated_" }; 

    public static final String NEW_ELEMENT_TAG = "@mbg.generated"; 
    public static final String[] OLD_ELEMENT_TAGS = {
            "@ibatorgenerated", 
            "@abatorgenerated", 
            "@mbggenerated", 
            "@mbg.generated" }; 

}
