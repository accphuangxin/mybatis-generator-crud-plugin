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
    private Map<InternalType, FullyQualifiedJavaType> fqjts;
    
    
    public GeneratedController(Map<InternalType, FullyQualifiedJavaType> fqjts){
        this.fqjts = fqjts;
    }
    
    public CompilationUnit generated(IntrospectedTable introspectedTable) {
       
        FullyQualifiedJavaType controllerType = fqjts.get(InternalType.ATTR_CONTROLLER_TYPE);
        
        TopLevelClass controller = new TopLevelClass(controllerType.getFullyQualifiedName());
        controller.setVisibility(JavaVisibility.PUBLIC);
    
        controller.addImportedType(fqjts.get(InternalType.ATTR_DTO_TYPE));
        controller.addImportedType(fqjts.get(InternalType.ATTR_SERVICE_TYPE));
        controller.addImportedType(fqjts.get(InternalType.ATTR_PO_TYPE));
        controller.addImportedType("io.swagger.annotations.Api");
        controller.addImportedType("io.swagger.annotations.ApiOperation");
        controller.addImportedType("org.ffm.saas.smarterp.common.model.DataResult");
        controller.addImportedType("org.ffm.saas.smarterp.common.model.PageRequest");
        controller.addImportedType("org.ffm.saas.smarterp.common.model.PageResponse");
        controller.addImportedType("org.springframework.beans.BeanUtils");
        controller.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        controller.addImportedType("org.springframework.web.bind.annotation.GetMapping");
        controller.addImportedType("org.springframework.web.bind.annotation.RequestBody");
        controller.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
        controller.addImportedType("org.springframework.web.bind.annotation.RequestMethod");
        controller.addImportedType("org.springframework.web.bind.annotation.RestController");
    
        String name = GeneratedCrudPlugin.lowerFirstChar(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
    
        //swagger注释.
        controller.addAnnotation("@Api(\"" + name + " Controller\")");
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
        FullyQualifiedJavaType serviceType = fqjts.get(InternalType.ATTR_SERVICE_TYPE);
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
        method.addAnnotation("@ApiOperation(\"" + name +" 创建\")");
        method.addAnnotation("@RequestMapping(value = \"/create\", method = RequestMethod.POST)");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("create");
        
    
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("org.ffm.saas.smarterp.common.model.DataResult");
        fqjt.addTypeArgument(new FullyQualifiedJavaType("java.lang.Boolean"));
        method.setReturnType(fqjt);
    
        FullyQualifiedJavaType dtoType = fqjts.get(InternalType.ATTR_DTO_TYPE);
        FullyQualifiedJavaType poType = fqjts.get(InternalType.ATTR_PO_TYPE);
        FullyQualifiedJavaType serviceType = fqjts.get(InternalType.ATTR_SERVICE_TYPE);
        
        String dtoRefName = GeneratedCrudPlugin.lowerFirstChar(dtoType.getShortName());
        String poRefName = GeneratedCrudPlugin.lowerFirstChar(poType.getShortName());
        String serviceRefName = GeneratedCrudPlugin.lowerFirstChar(serviceType.getShortName());
        Parameter parameter = new Parameter(dtoType, dtoRefName, "@RequestBody");
        
        method.addParameter(parameter);
        method.addBodyLine(String.format("%s %s = new %s();",poType.getShortName(), poRefName, poType.getShortName()));
        method.addBodyLine(String.format("BeanUtils.copyProperties(%s, %s);", dtoRefName, poRefName));
        method.addBodyLine(String.format("return DataResult.ok(%s.create(%s));", serviceRefName, poRefName));
        return method;
    }
    
    private Method getDeleteMethod(String name){
        Method method = new Method();
        method.addAnnotation("@ApiOperation(\"" + name +" 基于主键删除\")");
        method.addAnnotation("@GetMapping(\"/delete\")");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("delete");
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("org.ffm.saas.smarterp.common.model.DataResult");
        fqjt.addTypeArgument(new FullyQualifiedJavaType("java.lang.Boolean"));
        method.setReturnType(fqjt);
        
        FullyQualifiedJavaType serviceType = fqjts.get(InternalType.ATTR_SERVICE_TYPE);
        
        Parameter parameter = new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "id");
        method.addParameter(parameter);
        
        method.addBodyLine(String.format("return DataResult.ok(%s.delete(id));", GeneratedCrudPlugin.lowerFirstChar(serviceType.getShortName())));
        return method;
    }
    
    private Method getUpdateMethod(String name){
        Method method = new Method();
        method.addAnnotation("@ApiOperation(\"" + name +" 基于主键修改\")");
        method.addAnnotation("@RequestMapping(value = \"/update\", method = RequestMethod.POST)");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("update");
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("org.ffm.saas.smarterp.common.model.DataResult");
        fqjt.addTypeArgument(new FullyQualifiedJavaType("java.lang.Boolean"));
        method.setReturnType(fqjt);
    
        FullyQualifiedJavaType dtoType = fqjts.get(InternalType.ATTR_DTO_TYPE);
        FullyQualifiedJavaType poType = fqjts.get(InternalType.ATTR_PO_TYPE);
        FullyQualifiedJavaType serviceType = fqjts.get(InternalType.ATTR_SERVICE_TYPE);
    
        String dtoRefName = GeneratedCrudPlugin.lowerFirstChar(dtoType.getShortName());
        String poRefName = GeneratedCrudPlugin.lowerFirstChar(poType.getShortName());
        String serviceRefName = GeneratedCrudPlugin.lowerFirstChar(serviceType.getShortName());
        Parameter parameter = new Parameter(dtoType, dtoRefName, "@RequestBody");
    
        method.addParameter(parameter);
        method.addBodyLine(String.format("%s %s = new %s();",poType.getShortName(), poRefName, poType.getShortName()));
        method.addBodyLine(String.format("BeanUtils.copyProperties(%s, %s);", dtoRefName, poRefName));
        method.addBodyLine(String.format("return DataResult.ok(%s.update(%s));", serviceRefName, poRefName));
        return method;
    }
    
    private Method getQueryMethod(String name){
        Method method = new Method();
        method.addAnnotation("@ApiOperation(\"" + name +" 分页查询\")");
        method.addAnnotation("@RequestMapping(value = \"/queryByPage\", method = RequestMethod.POST)");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("queryByPage");
    
        FullyQualifiedJavaType serviceType = fqjts.get(InternalType.ATTR_SERVICE_TYPE);
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("org.ffm.saas.smarterp.common.model.DataResult");
        fqjt.addTypeArgument(fqjts.get(InternalType.ATTR_PAGERESPONSE_TYPE));
        method.setReturnType(fqjt);
    
        FullyQualifiedJavaType pageRequestFqjt =  fqjts.get(InternalType.ATTR_PAGEREQUEST_TYPE);
        
        Parameter parameter = new Parameter(pageRequestFqjt, "pageParam", "@RequestBody");
        
        method.addParameter(parameter);
        method.addBodyLine(String.format("%s pageParamPo = new %s();",pageRequestFqjt.getShortName(), pageRequestFqjt.getShortName()));
        method.addBodyLine("BeanUtils.copyProperties(pageParam, pageParamPo);");
        method.addBodyLine(String.format("return DataResult.ok(%s.queryByPage(pageParamPo));", GeneratedCrudPlugin.lowerFirstChar(serviceType.getShortName())));
        return method;
    }
 
    
   

}
