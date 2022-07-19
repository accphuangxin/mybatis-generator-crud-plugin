package com.softwareloop.mybatis.generator.plugins;

import com.softwareloop.mybatis.generator.plugins.GeneratedCrudPlugin.InternalType;
import java.util.Map;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class GeneratedService {
    private Map<InternalType, FullyQualifiedJavaType> fqjts;
    
    
    public GeneratedService(Map<InternalType, FullyQualifiedJavaType> fqjts){
        this.fqjts = fqjts;
    }
    
    public CompilationUnit generated(IntrospectedTable introspectedTable) {
       
        FullyQualifiedJavaType serviceType = fqjts.get(InternalType.ATTR_SERVICE_TYPE);
    
        Interface service = new Interface(serviceType.getFullyQualifiedName());
        service.setVisibility(JavaVisibility.PUBLIC);
    
        service.addImportedType(fqjts.get(InternalType.ATTR_PAGERESPONSE_TYPE));
        service.addImportedType(fqjts.get(InternalType.ATTR_PAGEREQUEST_TYPE));
        service.addImportedType(fqjts.get(InternalType.ATTR_PO_TYPE));
    
    
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
    
        FullyQualifiedJavaType poType = fqjts.get(InternalType.ATTR_PO_TYPE);
        
        String poRefName = GeneratedCrudPlugin.lowerFirstChar(poType.getShortName());
        Parameter parameter = new Parameter(poType, poRefName);
        
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
    
        FullyQualifiedJavaType poType = fqjts.get(InternalType.ATTR_PO_TYPE);
    
        String poRefName = GeneratedCrudPlugin.lowerFirstChar(poType.getShortName());
        Parameter parameter = new Parameter(poType, poRefName);
    
        method.addParameter(parameter);
        return method;
    }
    
    private Method getQueryMethod(){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("queryByPage");
        
        method.setReturnType(new FullyQualifiedJavaType(fqjts.get(InternalType.ATTR_PAGERESPONSE_TYPE).getShortName()));
        method.addParameter(new Parameter(new FullyQualifiedJavaType(fqjts.get(InternalType.ATTR_PAGEREQUEST_TYPE).getShortName()), "pageParam"));
        return method;
    }
 
    
   

}
