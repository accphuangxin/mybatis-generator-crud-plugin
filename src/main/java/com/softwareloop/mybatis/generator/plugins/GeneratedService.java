package com.softwareloop.mybatis.generator.plugins;

import com.softwareloop.mybatis.generator.plugins.GeneratedCrudPlugin.InternalType;
import java.util.Map;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

public class GeneratedService {
    private Map<InternalType, FullyQualifiedJavaType> types;
    
    
    public GeneratedService(Map<InternalType, FullyQualifiedJavaType> types){
        this.types = types;
    }
    
    public CompilationUnit generated(IntrospectedTable introspectedTable) {
       
        FullyQualifiedJavaType serviceType = types.get(InternalType.ATTR_SERVICE_TYPE);
    
        Interface service = new Interface(serviceType.getFullyQualifiedName());
        service.setVisibility(JavaVisibility.PUBLIC);
    
        service.addImportedType(types.get(InternalType.ATTR_PAGERESPONSE_TYPE));
        service.addImportedType(types.get(InternalType.ATTR_PAGEREQUEST_TYPE));
        service.addImportedType(types.get(InternalType.ATTR_DTO_TYPE));
    
    
        service.addMethod(getQueryMethod());
        service.addMethod(getCreteMethod());
        service.addMethod(getUpdateMethod());
        service.addMethod(getDeleteMethod());
        return service;
    }
    
    private Method getCreteMethod(){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("create");
        
        method.setReturnType(new FullyQualifiedJavaType("java.lang.Boolean"));
    
        FullyQualifiedJavaType dtoType = types.get(InternalType.ATTR_DTO_TYPE);
        
        String dtoRefName = GeneratedCrudPlugin.lowerFirstChar(dtoType.getShortName());
        Parameter parameter = new Parameter(dtoType, dtoRefName);
        
        method.addParameter(parameter);
        return method;
    }
    
    private Method getDeleteMethod(){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("delete");
        
        method.setReturnType(new FullyQualifiedJavaType("java.lang.Boolean"));
        
        Parameter parameter = new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "id");
        method.addParameter(parameter);
        return method;
    }
    
    private Method getUpdateMethod(){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("update");
    
        method.setReturnType(new FullyQualifiedJavaType("java.lang.Boolean"));
    
        FullyQualifiedJavaType dtoType = types.get(InternalType.ATTR_DTO_TYPE);
    
        String dtoRefName = GeneratedCrudPlugin.lowerFirstChar(dtoType.getShortName());
        Parameter parameter = new Parameter(dtoType, dtoRefName);
    
        method.addParameter(parameter);
        return method;
    }
    
    private Method getQueryMethod(){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("queryByPage");
        
        method.setReturnType(new FullyQualifiedJavaType(types.get(InternalType.ATTR_PAGERESPONSE_TYPE).getShortName()));
        method.addParameter(new Parameter(new FullyQualifiedJavaType(types.get(InternalType.ATTR_PAGEREQUEST_TYPE).getShortName()), "pageParam"));
        return method;
    }
 
    
   

}
