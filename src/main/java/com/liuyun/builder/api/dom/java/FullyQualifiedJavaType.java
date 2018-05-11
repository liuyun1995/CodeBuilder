package com.liuyun.builder.api.dom.java;

import static com.liuyun.builder.internal.utils.StringUtil.stringHasValue;
import static com.liuyun.builder.internal.utils.messages.Messages.getString;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//全限定Java类型
public class FullyQualifiedJavaType implements Comparable<FullyQualifiedJavaType> {
    
    private static final String JAVA_LANG = "java.lang"; 
    private static FullyQualifiedJavaType intInstance = null;
    private static FullyQualifiedJavaType stringInstance = null;
    private static FullyQualifiedJavaType booleanPrimitiveInstance = null;
    private static FullyQualifiedJavaType objectInstance = null;
    private static FullyQualifiedJavaType dateInstance = null;
    private static FullyQualifiedJavaType criteriaInstance = null;
    private static FullyQualifiedJavaType generatedCriteriaInstance = null;
    private String baseShortName;                        //简单名称(带类型参数)
    private String baseQualifiedName;                    //全限定名称(不带类型参数)
    private boolean explicitlyImported;                 //是否是具体的导入
    private String packageName;                          //包名
    private boolean primitive;                          //是否是基本类型
    private boolean isArray;                            //是否是数组类型
    private PrimitiveTypeWrapper primitiveTypeWrapper;   //基础类型包装器
    private List<FullyQualifiedJavaType> typeArguments;  //类型参数集合
    private boolean wildcardType;                       //是否是泛型类型
    private boolean boundedWildcard;                    //是否是有界泛型类型
    private boolean extendsBoundedWildcard;             //是否是继承的有界泛型类型
    
    public FullyQualifiedJavaType(String fullTypeSpecification) {
        super();
        typeArguments = new ArrayList<FullyQualifiedJavaType>();
        //解析全限定类名
        parse(fullTypeSpecification);
    }

    //是否是具体的导入
    public boolean isExplicitlyImported() {
        return explicitlyImported;
    }
    
    //获取导入集合
    public List<String> getImportList() {
        List<String> answer = new ArrayList<String>();
        if (isExplicitlyImported()) {
            int index = baseShortName.indexOf('.');
            if (index == -1) {
                answer.add(calculateActualImport(baseQualifiedName));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(packageName);
                sb.append('.');
                sb.append(calculateActualImport(baseShortName.substring(0, index)));
                answer.add(sb.toString());
            }
        }
        for (FullyQualifiedJavaType fqjt : typeArguments) {
            answer.addAll(fqjt.getImportList());
        }
        return answer;
    }

    //计算实际导入
    private String calculateActualImport(String name) {
        String answer = name;
        if (this.isArray()) {
            int index = name.indexOf("["); 
            if (index != -1) {
                answer = name.substring(0, index);
            }
        }
        return answer;
    }

    //获取包名
    public String getPackageName() {
        return packageName;
    }
    
    //获取简单名称(带类型参数)
    public String getShortName() {
        StringBuilder sb = new StringBuilder();
        if (wildcardType) {
            sb.append('?');
            if (boundedWildcard) {
                if (extendsBoundedWildcard) {
                    sb.append(" extends ");
                } else {
                    sb.append(" super ");
                }
                sb.append(baseShortName);
            }
        } else {
            sb.append(baseShortName);
        }
        if (typeArguments.size() > 0) {
            boolean first = true;
            sb.append('<');
            for (FullyQualifiedJavaType fqjt : typeArguments) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                sb.append(fqjt.getShortName());
            }
            sb.append('>');
        }
        return sb.toString();
    }

    //获取简单名称(不带类型参数)
    public String getShortNameWithoutTypeArguments() {
        return baseShortName;
    }
    
    //返回全限定名(带类型参数)
    public String getFullyQualifiedName() {
        StringBuilder sb = new StringBuilder();
        if (wildcardType) {
            sb.append('?');
            if (boundedWildcard) {
                if (extendsBoundedWildcard) {
                    sb.append(" extends "); 
                } else {
                    sb.append(" super "); 
                }
                sb.append(baseQualifiedName);
            }
        } else {
            sb.append(baseQualifiedName);
        }
        if (typeArguments.size() > 0) {
            boolean first = true;
            sb.append('<');
            for (FullyQualifiedJavaType fqjt : typeArguments) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", "); 
                }
                sb.append(fqjt.getFullyQualifiedName());
            }
            sb.append('>');
        }
        return sb.toString();
    }

    //获取全限定名(不带类型参数)
    public String getFullyQualifiedNameWithoutTypeParameters() {
        return baseQualifiedName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FullyQualifiedJavaType)) {
            return false;
        }
        FullyQualifiedJavaType other = (FullyQualifiedJavaType) obj;
        return getFullyQualifiedName().equals(other.getFullyQualifiedName());
    }

    @Override
    public int hashCode() {
        return getFullyQualifiedName().hashCode();
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
    }

    //是否是基本类型
    public boolean isPrimitive() {
        return primitive;
    }

    //获取基础类型包装类
    public PrimitiveTypeWrapper getPrimitiveTypeWrapper() {
        return primitiveTypeWrapper;
    }

    //获取int实例
    public static final FullyQualifiedJavaType getIntInstance() {
        if (intInstance == null) {
            intInstance = new FullyQualifiedJavaType("int"); 
        }
        return intInstance;
    }

    //获取Map实例
    public static final FullyQualifiedJavaType getNewMapInstance() {
        return new FullyQualifiedJavaType("java.util.Map"); 
    }

    //获取List实例
    public static final FullyQualifiedJavaType getNewListInstance() {
        return new FullyQualifiedJavaType("java.util.List"); 
    }

    //获取HashMap实例
    public static final FullyQualifiedJavaType getNewHashMapInstance() {
        return new FullyQualifiedJavaType("java.util.HashMap"); 
    }

    //获取ArrayList实例
    public static final FullyQualifiedJavaType getNewArrayListInstance() {
        return new FullyQualifiedJavaType("java.util.ArrayList");
    }

    //获取Iterator实例
    public static final FullyQualifiedJavaType getNewIteratorInstance() {
        return new FullyQualifiedJavaType("java.util.Iterator");
    }

    //获取String实例
    public static final FullyQualifiedJavaType getStringInstance() {
        if (stringInstance == null) {
            stringInstance = new FullyQualifiedJavaType("java.lang.String");
        }
        return stringInstance;
    }

    //获取Boolean实例
    public static final FullyQualifiedJavaType getBooleanPrimitiveInstance() {
        if (booleanPrimitiveInstance == null) {
            booleanPrimitiveInstance = new FullyQualifiedJavaType("boolean");
        }
        return booleanPrimitiveInstance;
    }

    //获取Object实例
    public static final FullyQualifiedJavaType getObjectInstance() {
        if (objectInstance == null) {
            objectInstance = new FullyQualifiedJavaType("java.lang.Object");
        }
        return objectInstance;
    }

    //获取Date实例
    public static final FullyQualifiedJavaType getDateInstance() {
        if (dateInstance == null) {
            dateInstance = new FullyQualifiedJavaType("java.util.Date");
        }
        return dateInstance;
    }

    //获取Criteria实例
    public static final FullyQualifiedJavaType getCriteriaInstance() {
        if (criteriaInstance == null) {
            criteriaInstance = new FullyQualifiedJavaType("Criteria");
        }
        return criteriaInstance;
    }

    //获取GeneratedCriteria实例
    public static final FullyQualifiedJavaType getGeneratedCriteriaInstance() {
        if (generatedCriteriaInstance == null) {
            generatedCriteriaInstance = new FullyQualifiedJavaType("GeneratedCriteria"); 
        }
        return generatedCriteriaInstance;
    }

    @Override
    public int compareTo(FullyQualifiedJavaType other) {
        return getFullyQualifiedName().compareTo(other.getFullyQualifiedName());
    }

    //添加类型参数
    public void addTypeArgument(FullyQualifiedJavaType type) {
        typeArguments.add(type);
    }

    private void parse(String fullTypeSpecification) {
    	//去除前后空格
        String spec = fullTypeSpecification.trim();
        //是否是？开头
        if (spec.startsWith("?")) {
        	//通配符类型
            wildcardType = true;
            //裁去？并去掉前后空格
            spec = spec.substring(1).trim();
            //是否是"extends "开头
            if (spec.startsWith("extends ")) { 
                boundedWildcard = true;
                extendsBoundedWildcard = true;
                //裁去"extends "
                spec = spec.substring(8);
            //是否是"super "开头
            } else if (spec.startsWith("super ")) { 
                boundedWildcard = true;
                extendsBoundedWildcard = false;
                //裁去"super "
                spec = spec.substring(6);
            } else {
            	//非有界泛型
                boundedWildcard = false;
            }
            parse(spec);
        } else {
        	//获取"<"的位置
            int index = fullTypeSpecification.indexOf('<');
            //如果没有"<"就表明不是泛型类型
            if (index == -1) {
            	//使用简单解析
                simpleParse(fullTypeSpecification);
            } else {
                simpleParse(fullTypeSpecification.substring(0, index));
                int endIndex = fullTypeSpecification.lastIndexOf('>');
                if (endIndex == -1) {
                    throw new RuntimeException(getString("RuntimeError.22", fullTypeSpecification)); 
                }
                genericParse(fullTypeSpecification.substring(index, endIndex + 1));
            }
            isArray = fullTypeSpecification.endsWith("]"); 
        }
    }

    //简单解析
    private void simpleParse(String typeSpecification) {
        baseQualifiedName = typeSpecification.trim();
        //是否包含"."
        if (baseQualifiedName.contains(".")) {
        	//获取包名
            packageName = getPackage(baseQualifiedName);
            //获取简短名称
            baseShortName = baseQualifiedName.substring(packageName.length() + 1);
            //再检测"."的位置
            int index = baseShortName.lastIndexOf('.');
            if (index != -1) {
                baseShortName = baseShortName.substring(index + 1);
            }
            if (JAVA_LANG.equals(packageName)) { 
                explicitlyImported = false;
            } else {
                explicitlyImported = true;
            }
        } else {
            baseShortName = baseQualifiedName;
            explicitlyImported = false;
            packageName = ""; 
            if ("byte".equals(baseQualifiedName)) { 
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getByteInstance();
            } else if ("short".equals(baseQualifiedName)) { 
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getShortInstance();
            } else if ("int".equals(baseQualifiedName)) { 
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getIntegerInstance();
            } else if ("long".equals(baseQualifiedName)) { 
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getLongInstance();
            } else if ("char".equals(baseQualifiedName)) { 
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getCharacterInstance();
            } else if ("float".equals(baseQualifiedName)) { 
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getFloatInstance();
            } else if ("double".equals(baseQualifiedName)) { 
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getDoubleInstance();
            } else if ("boolean".equals(baseQualifiedName)) { 
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getBooleanInstance();
            } else {
                primitive = false;
                primitiveTypeWrapper = null;
            }
        }
    }

    //解析泛型
    private void genericParse(String genericSpecification) {
        int lastIndex = genericSpecification.lastIndexOf('>');
        if (lastIndex == -1) {
            throw new RuntimeException(getString("RuntimeError.22", genericSpecification)); 
        }
        String argumentString = genericSpecification.substring(1, lastIndex);
        StringTokenizer st = new StringTokenizer(argumentString, ",<>", true); 
        int openCount = 0;
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("<".equals(token)) { 
                sb.append(token);
                openCount++;
            } else if (">".equals(token)) { 
                sb.append(token);
                openCount--;
            } else if (",".equals(token)) { 
                if (openCount == 0) {
                    typeArguments.add(new FullyQualifiedJavaType(sb.toString()));
                    sb.setLength(0);
                } else {
                    sb.append(token);
                }
            } else {
                sb.append(token);
            }
        }
        if (openCount != 0) {
            throw new RuntimeException(getString("RuntimeError.22", genericSpecification)); 
        }
        String finalType = sb.toString();
        if (stringHasValue(finalType)) {
            typeArguments.add(new FullyQualifiedJavaType(finalType));
        }
    }

    //根据全限定类名获取包名
    private static String getPackage(String baseQualifiedName) {
    	//获取最后位置的"."
        int index = baseQualifiedName.lastIndexOf('.');
        return baseQualifiedName.substring(0, index);
    }

    //是否是数组
    public boolean isArray() {
        return isArray;
    }

    //获取类型参数
    public List<FullyQualifiedJavaType> getTypeArguments() {
        return typeArguments;
    }
}
