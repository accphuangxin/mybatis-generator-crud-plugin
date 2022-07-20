package com.softwareloop.mybatis.generator.plugins;

import com.softwareloop.mybatis.generator.plugins.GeneratedCrudPlugin.InternalType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;

public class GeneratedController {
    private Map<InternalType, FullyQualifiedJavaType> types;
    
    
    public GeneratedController(Map<InternalType, FullyQualifiedJavaType> types){
        this.types = types;
    }
    
    public CompilationUnit generated(IntrospectedTable introspectedTable) {
       
        FullyQualifiedJavaType controllerType = types.get(InternalType.ATTR_CONTROLLER_TYPE);
        
        TopLevelClass controller = new TopLevelClass(controllerType.getFullyQualifiedName());
        controller.setVisibility(JavaVisibility.PUBLIC);
    
        controller.addImportedType(types.get(InternalType.ATTR_DTO_TYPE));
        controller.addImportedType(types.get(InternalType.ATTR_SERVICE_TYPE));
        controller.addImportedType("io.swagger.annotations.Api");
        controller.addImportedType("io.swagger.annotations.ApiOperation");
        controller.addImportedType("org.ffm.saas.smarterp.common.model.DataResult");
        controller.addImportedType("org.ffm.saas.smarterp.common.model.PageRequest");
        controller.addImportedType("org.ffm.saas.smarterp.common.model.PageResponse");
        controller.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        controller.addImportedType("org.springframework.web.bind.annotation.GetMapping");
        controller.addImportedType("org.springframework.web.bind.annotation.RequestBody");
        controller.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
        controller.addImportedType("org.springframework.web.bind.annotation.RequestMethod");
        controller.addImportedType("org.springframework.web.bind.annotation.RestController");
    
        String name = GeneratedCrudPlugin.lowerFirstChar(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
    
        //swagger注释.
        controller.addAnnotation("@Api(\"" + name + " 服务列表\")");
        controller.addAnnotation("@RestController");
        controller.addAnnotation("@RequestMapping(\"/system/"+name+"\")");
        
        controller.addField(getField());
    
        controller.addMethod(getQueryMethod(name));
        controller.addMethod(getCreteMethod(name));
        controller.addMethod(getUpdateMethod(name));
        controller.addMethod(getDeleteMethod(name));
        return controller;
    }
    
    private Field getField(){
        FullyQualifiedJavaType serviceType = types.get(InternalType.ATTR_SERVICE_TYPE);
        String servicesRefName = GeneratedCrudPlugin.lowerFirstChar(serviceType.getShortName());
     
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setName(servicesRefName);
        field.setType(new FullyQualifiedJavaType(serviceType.getShortName()));
        field.addAnnotation("@Autowired");
        return field;
    }
    
    private Method getCreteMethod(String name){
        Method method = new Method();
        method.addAnnotation("@ApiOperation(\"" + name +" 创建服务\")");
        method.addAnnotation("@RequestMapping(value = \"/create\", method = RequestMethod.POST)");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("create");
        
    
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("org.ffm.saas.smarterp.common.model.DataResult");
        fqjt.addTypeArgument(new FullyQualifiedJavaType("java.lang.Boolean"));
        method.setReturnType(fqjt);
    
        FullyQualifiedJavaType dtoType = types.get(InternalType.ATTR_DTO_TYPE);
        FullyQualifiedJavaType serviceType = types.get(InternalType.ATTR_SERVICE_TYPE);
        
        String dtoRefName = GeneratedCrudPlugin.lowerFirstChar(dtoType.getShortName());
        String serviceRefName = GeneratedCrudPlugin.lowerFirstChar(serviceType.getShortName());
        
        Parameter parameter = new Parameter(dtoType, dtoRefName, "@RequestBody");
        
        method.addParameter(parameter);
        method.addBodyLine(String.format("return DataResult.ok(%s.create(%s));", serviceRefName, dtoRefName));
        return method;
    }
    
    private Method getDeleteMethod(String name){
        Method method = new Method();
        method.addAnnotation("@ApiOperation(\"" + name +" 删除服务\")");
        method.addAnnotation("@GetMapping(\"/delete\")");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("delete");
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("org.ffm.saas.smarterp.common.model.DataResult");
        fqjt.addTypeArgument(new FullyQualifiedJavaType("java.lang.Boolean"));
        method.setReturnType(fqjt);
        
        FullyQualifiedJavaType serviceType = types.get(InternalType.ATTR_SERVICE_TYPE);
        
        Parameter parameter = new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "id");
        method.addParameter(parameter);
        
        method.addBodyLine(String.format("return DataResult.ok(%s.delete(id));", GeneratedCrudPlugin.lowerFirstChar(serviceType.getShortName())));
        return method;
    }
    
    private Method getUpdateMethod(String name){
        Method method = new Method();
        method.addAnnotation("@ApiOperation(\"" + name +" 修改服务\")");
        method.addAnnotation("@RequestMapping(value = \"/update\", method = RequestMethod.POST)");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("update");
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("org.ffm.saas.smarterp.common.model.DataResult");
        fqjt.addTypeArgument(new FullyQualifiedJavaType("java.lang.Boolean"));
        method.setReturnType(fqjt);
    
        FullyQualifiedJavaType dtoType = types.get(InternalType.ATTR_DTO_TYPE);
        FullyQualifiedJavaType poType = types.get(InternalType.ATTR_PO_TYPE);
        FullyQualifiedJavaType serviceType = types.get(InternalType.ATTR_SERVICE_TYPE);
    
        String dtoRefName = GeneratedCrudPlugin.lowerFirstChar(dtoType.getShortName());
        String serviceRefName = GeneratedCrudPlugin.lowerFirstChar(serviceType.getShortName());
        Parameter parameter = new Parameter(dtoType, dtoRefName, "@RequestBody");
    
        method.addParameter(parameter);
        method.addBodyLine(String.format("return DataResult.ok(%s.update(%s));", serviceRefName, dtoRefName));
        return method;
    }
    
    private Method getQueryMethod(String name){
        Method method = new Method();
        method.addAnnotation("@ApiOperation(\"" + name +" 分页查询\")");
        method.addAnnotation("@RequestMapping(value = \"/queryByPage\", method = RequestMethod.POST)");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("queryByPage");
    
        FullyQualifiedJavaType serviceType = types.get(InternalType.ATTR_SERVICE_TYPE);
        
        FullyQualifiedJavaType resultType = new FullyQualifiedJavaType("org.ffm.saas.smarterp.common.model.DataResult");
        resultType.addTypeArgument(types.get(InternalType.ATTR_PAGERESPONSE_TYPE));
        method.setReturnType(resultType);
    
        FullyQualifiedJavaType pageRequestType =  types.get(InternalType.ATTR_PAGEREQUEST_TYPE);
        
        Parameter parameter = new Parameter(pageRequestType, "pageParam", "@RequestBody");
        
        method.addParameter(parameter);
        method.addBodyLine(String.format("return DataResult.ok(%s.queryByPage(pageParam));", GeneratedCrudPlugin.lowerFirstChar(serviceType.getShortName())));
        return method;
    }
 
    
   

}
