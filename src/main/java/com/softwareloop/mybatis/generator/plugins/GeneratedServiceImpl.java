package com.softwareloop.mybatis.generator.plugins;

import com.softwareloop.mybatis.generator.plugins.GeneratedCrudPlugin.InternalType;
import java.util.Map;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class GeneratedServiceImpl {
    private Map<InternalType, FullyQualifiedJavaType> types;
    
    
    public GeneratedServiceImpl(Map<InternalType, FullyQualifiedJavaType> types){
        this.types = types;
    }
    
    public CompilationUnit generated(IntrospectedTable introspectedTable) {
       
        FullyQualifiedJavaType serviceImplType = types.get(InternalType.ATTR_SERVICE_IMPL_TYPE);
        FullyQualifiedJavaType serviceType = types.get(InternalType.ATTR_SERVICE_TYPE);
    
        TopLevelClass serviceImpl = new TopLevelClass(serviceImplType.getFullyQualifiedName());
        serviceImpl.addSuperInterface(serviceType);
        
        serviceImpl.setVisibility(JavaVisibility.PUBLIC);
        
        
    
        serviceImpl.addImportedType(types.get(InternalType.ATTR_PAGERESPONSE_TYPE));
        serviceImpl.addImportedType(types.get(InternalType.ATTR_PAGEREQUEST_TYPE));
        serviceImpl.addImportedType(types.get(InternalType.ATTR_PO_TYPE));
        serviceImpl.addImportedType("org.ffm.saas.smarterp.common.util.BeanUtilsWrapper");
        serviceImpl.addImportedType(serviceType);
        serviceImpl.addImportedType(types.get(InternalType.ATTR_DAO_TYPE));
        serviceImpl.addImportedType(types.get(InternalType.ATTR_DTO_TYPE));
        serviceImpl.addImportedType("java.util.stream.Collectors");
        serviceImpl.addImportedType("com.github.pagehelper.PageHelper");
        serviceImpl.addImportedType("com.github.pagehelper.PageInfo");
        serviceImpl.addImportedType("com.github.pagehelper.PageInfo");
        serviceImpl.addImportedType("java.util.List");
        serviceImpl.addImportedType("lombok.extern.slf4j.Slf4j");
        serviceImpl.addImportedType("org.ffm.saas.smarterp.common.exception.AppException");
        serviceImpl.addImportedType("org.springframework.beans.factory.annotation.Autowired");
        serviceImpl.addImportedType("org.springframework.stereotype.Service");
    
    
        serviceImpl.addAnnotation("@Service");
        serviceImpl.addAnnotation("@Slf4j");
        
        serviceImpl.addField(getField());
    
        String name = GeneratedCrudPlugin.lowerFirstChar(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        
        serviceImpl.addMethod(getQueryMethod());
        serviceImpl.addMethod(getCreteMethod(name));
        serviceImpl.addMethod(getUpdateMethod(name));
        serviceImpl.addMethod(getDeleteMethod());
        return serviceImpl;
    }
    
    private Field getField(){
        FullyQualifiedJavaType daoType = types.get(InternalType.ATTR_DAO_TYPE);
        String daoTypeRefName = GeneratedCrudPlugin.lowerFirstChar(daoType.getShortName());
        
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setName(daoTypeRefName);
        field.setType(new FullyQualifiedJavaType(daoType.getShortName()));
        field.addAnnotation("@Autowired");
        return field;
    }
    
    private Method getCreteMethod(String name){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("create");
        method.addAnnotation("@Override");
        
        method.setReturnType(new FullyQualifiedJavaType("java.lang.Boolean"));
    
        FullyQualifiedJavaType poType = types.get(InternalType.ATTR_PO_TYPE);
        FullyQualifiedJavaType dtoType = types.get(InternalType.ATTR_DTO_TYPE);
        FullyQualifiedJavaType daoType = types.get(InternalType.ATTR_DAO_TYPE);
        
        String dtoRefName = GeneratedCrudPlugin.lowerFirstChar(dtoType.getShortName());
        String daoRefName = GeneratedCrudPlugin.lowerFirstChar(daoType.getShortName());
        
        Parameter parameter = new Parameter(dtoType, dtoRefName);
        method.addParameter(parameter);
    
        method.addBodyLine(String.format("if (%s == null){", dtoRefName));
        method.addBodyLine(String.format("throw new AppException(\"%s 参数不能为空!\");", name));
        method.addBodyLine("}");
        
        method.addBodyLine(String.format("return %s.insert(BeanUtilsWrapper.copy(%s, new %s())) > 0 ? true : false;", daoRefName, dtoRefName, poType.getShortName()));
        
        return method;
    }
    
    private Method getDeleteMethod(){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("delete");
        method.addAnnotation("@Override");
        
        method.setReturnType(new FullyQualifiedJavaType("java.lang.Boolean"));
        
        Parameter parameter = new Parameter(new FullyQualifiedJavaType("java.lang.Integer"), "id");
        method.addParameter(parameter);
    
        FullyQualifiedJavaType daoType = types.get(InternalType.ATTR_DAO_TYPE);
    
        String daoRefName = GeneratedCrudPlugin.lowerFirstChar(daoType.getShortName());
    
        method.addBodyLine("if (id == null){");
        method.addBodyLine("throw new AppException(\"id 参数不能为空!\");");
        method.addBodyLine("}");
    
        method.addBodyLine(String.format("return %s.deleteByPrimaryKey(id) > 0 ? true : false;", daoRefName));
        
        return method;
    }
    
    private Method getUpdateMethod(String name){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("update");
        method.addAnnotation("@Override");
    
        method.setReturnType(new FullyQualifiedJavaType("java.lang.Boolean"));
    
        FullyQualifiedJavaType poType = types.get(InternalType.ATTR_PO_TYPE);
        FullyQualifiedJavaType dtoType = types.get(InternalType.ATTR_DTO_TYPE);
    
        String dtoRefName = GeneratedCrudPlugin.lowerFirstChar(dtoType.getShortName());
        Parameter parameter = new Parameter(dtoType, dtoRefName);
    
        method.addParameter(parameter);
    
        FullyQualifiedJavaType daoType = types.get(InternalType.ATTR_DAO_TYPE);
        String daoRefName = GeneratedCrudPlugin.lowerFirstChar(daoType.getShortName());
   
    
        method.addBodyLine(String.format("if (%s == null){", dtoRefName));
        method.addBodyLine(String.format("throw new AppException(\"%s 参数不能为空!\");", name));
        method.addBodyLine("}");
    
        method.addBodyLine(String.format("return %s.updateByPrimaryKey(BeanUtilsWrapper.copy(%s, new %s())) > 0 ? true : false;", daoRefName, dtoRefName, poType.getShortName()));
        return method;
    }
    
    private Method getQueryMethod(){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("queryByPage");
        method.addAnnotation("@Override");
        
        method.setReturnType(new FullyQualifiedJavaType(types.get(InternalType.ATTR_PAGERESPONSE_TYPE).getShortName()));
        method.addParameter(new Parameter(new FullyQualifiedJavaType(types.get(InternalType.ATTR_PAGEREQUEST_TYPE).getShortName()), "pageParam"));
    
        FullyQualifiedJavaType poType = types.get(InternalType.ATTR_PO_TYPE);
        FullyQualifiedJavaType dtoType = types.get(InternalType.ATTR_DTO_TYPE);
        String poRefName = GeneratedCrudPlugin.lowerFirstChar(poType.getShortName());
    
        FullyQualifiedJavaType daoType = types.get(InternalType.ATTR_DAO_TYPE);
        String daoRefName = GeneratedCrudPlugin.lowerFirstChar(daoType.getShortName());
        
        method.addBodyLine("PageHelper.startPage(pageParam.getPageNum(),pageParam.getPageSize());");
        method.addBodyLine(String.format("List<%s> %sList = %s.selectAll();",poType.getShortName(), poRefName, daoRefName));
        
        method.addBodyLine(String.format("List<%s> dtoList = %sList.stream().map(po ->{",dtoType.getShortName(), poRefName));
        method.addBodyLine(String.format("return BeanUtilsWrapper.copy(po, new %s());", dtoType.getShortName()));
        method.addBodyLine("}).collect(Collectors.toList());");
        method.addBodyLine(String.format("PageInfo<%s> pageInfo = new PageInfo<>(dtoList);", dtoType.getShortName()));
        method.addBodyLine(String.format("return PageResponse.<%s>builder(pageInfo);", dtoType.getShortName()));
        return method;
    }
 
    
   

}
