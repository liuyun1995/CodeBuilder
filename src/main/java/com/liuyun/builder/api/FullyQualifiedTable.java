package com.liuyun.builder.api;

import static com.liuyun.builder.internal.utils.EqualsUtil.areEqual;
import static com.liuyun.builder.internal.utils.HashCodeUtil.SEED;
import static com.liuyun.builder.internal.utils.HashCodeUtil.hash;
import static com.liuyun.builder.internal.utils.JavaBeansUtil.getCamelCaseString;
import static com.liuyun.builder.internal.utils.StringUtil.composeFullyQualifiedTableName;
import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.liuyun.builder.config.DomainObjectRenamingRule;
import com.liuyun.builder.config.label.Context;

//唯一全限定表
public class FullyQualifiedTable {

    private String introspectedCatalog;                          //逆向目录
    private String introspectedSchema;                           //逆向概要
    private String introspectedTableName;                        //逆向表名
    private String runtimeCatalog;                               //运行时目录
    private String runtimeSchema;                                //运行时概要
    private String runtimeTableName;                             //运行时表名
    private String domainObjectName;                             //JavaBean名
    private String domainObjectSubPackage;                       //JavaBean的子包
    private String alias;                                        //表的别名
    private boolean ignoreQualifiersAtRuntime;                   //运行时忽略特定的表
    private String beginningDelimiter;                           //开始的分隔符
    private String endingDelimiter;                              //结尾的分隔符
    private DomainObjectRenamingRule domainObjectRenamingRule;   //JavaBean重命名规则
    
    public FullyQualifiedTable(String introspectedCatalog,
            String introspectedSchema, String introspectedTableName,
            String domainObjectName, String alias,
            boolean ignoreQualifiersAtRuntime, String runtimeCatalog,
            String runtimeSchema, String runtimeTableName,
            boolean delimitIdentifiers, DomainObjectRenamingRule domainObjectRenamingRule,
            Context context) {
        super();
        this.introspectedCatalog = introspectedCatalog;
        this.introspectedSchema = introspectedSchema;
        this.introspectedTableName = introspectedTableName;
        this.ignoreQualifiersAtRuntime = ignoreQualifiersAtRuntime;
        this.runtimeCatalog = runtimeCatalog;
        this.runtimeSchema = runtimeSchema;
        this.runtimeTableName = runtimeTableName;
        this.domainObjectRenamingRule = domainObjectRenamingRule;
        if (stringHasValue(domainObjectName)) {
            int index = domainObjectName.lastIndexOf('.');
            if (index == -1) {
                this.domainObjectName = domainObjectName;
            } else {
                this.domainObjectName = domainObjectName.substring(index + 1);
                this.domainObjectSubPackage = domainObjectName.substring(0, index);
            }
        }
        if (alias == null) {
            this.alias = null;
        } else {
            this.alias = alias.trim();
        }
        beginningDelimiter = delimitIdentifiers ? context.getBeginningDelimiter() : ""; 
        endingDelimiter = delimitIdentifiers ? context.getEndingDelimiter() : ""; 
    }
    
    public String getIntrospectedCatalog() {
        return introspectedCatalog;
    }
    
    public String getIntrospectedSchema() {
        return introspectedSchema;
    }
    
    public String getIntrospectedTableName() {
        return introspectedTableName;
    }
    
    //运行时获取表名
    public String getFullyQualifiedTableNameAtRuntime() {
        StringBuilder localCatalog = new StringBuilder();
        if (!ignoreQualifiersAtRuntime) {
            if (stringHasValue(runtimeCatalog)) {
                localCatalog.append(runtimeCatalog);
            } else if (stringHasValue(introspectedCatalog)) {
                localCatalog.append(introspectedCatalog);
            }
        }
        if (localCatalog.length() > 0) {
            addDelimiters(localCatalog);
        }
        StringBuilder localSchema = new StringBuilder();
        if (!ignoreQualifiersAtRuntime) {
            if (stringHasValue(runtimeSchema)) {
                localSchema.append(runtimeSchema);
            } else if (stringHasValue(introspectedSchema)) {
                localSchema.append(introspectedSchema);
            }
        }
        if (localSchema.length() > 0) {
            addDelimiters(localSchema);
        }
        StringBuilder localTableName = new StringBuilder();
        if (stringHasValue(runtimeTableName)) {
            localTableName.append(runtimeTableName);
        } else {
            localTableName.append(introspectedTableName);
        }
        addDelimiters(localTableName);
        return composeFullyQualifiedTableName(localCatalog.toString(), localSchema.toString(), localTableName.toString(), '.');
    }
    
    //运行时获取表别名
    public String getAliasedFullyQualifiedTableNameAtRuntime() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullyQualifiedTableNameAtRuntime());
        if (stringHasValue(alias)) {
            sb.append(' ');
            sb.append(alias);
        }
        return sb.toString();
    }
    
    //获取表对应的对象名
    public String getDomainObjectName() {
        if (stringHasValue(domainObjectName)) {
            return domainObjectName;
        }
        String finalDomainObjectName;
        if (stringHasValue(runtimeTableName)) {
            finalDomainObjectName =  getCamelCaseString(runtimeTableName, true);
        } else {
            finalDomainObjectName =  getCamelCaseString(introspectedTableName, true);
        }
        if (domainObjectRenamingRule != null) {
            Pattern pattern = Pattern.compile(domainObjectRenamingRule.getSearchString());
            String replaceString = domainObjectRenamingRule.getReplaceString();
            replaceString = replaceString == null ? "" : replaceString; 
            Matcher matcher = pattern.matcher(finalDomainObjectName);
            finalDomainObjectName = matcher.replaceAll(replaceString);
        }
        return finalDomainObjectName;
    }
    
    public String getAlias() {
        return alias;
    }
    
    //添加分隔符
    private void addDelimiters(StringBuilder sb) {
    	//插入起始分隔符
        if (stringHasValue(beginningDelimiter)) {
            sb.insert(0, beginningDelimiter);
        }
        //插入结尾分隔符
        if (stringHasValue(endingDelimiter)) {
            sb.append(endingDelimiter);
        }
    }

    //获取生成的domain的子包
    public String getDomainObjectSubPackage() {
        return domainObjectSubPackage;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FullyQualifiedTable)) {
            return false;
        }
        FullyQualifiedTable other = (FullyQualifiedTable) obj;
        return areEqual(this.introspectedTableName, other.introspectedTableName)
                && areEqual(this.introspectedCatalog, other.introspectedCatalog)
                && areEqual(this.introspectedSchema, other.introspectedSchema);
    }
    
    @Override
    public int hashCode() {
        int result = SEED;
        result = hash(result, introspectedTableName);
        result = hash(result, introspectedCatalog);
        result = hash(result, introspectedSchema);
        return result;
    }
    
    @Override
    public String toString() {
        return composeFullyQualifiedTableName(introspectedCatalog, introspectedSchema, introspectedTableName, '.');
    }
    
}
