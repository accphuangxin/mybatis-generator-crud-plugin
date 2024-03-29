package com.softwareloop.mybatis.generator.plugins;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.PropertyRegistry;

public class GeneratedCrudPlugin extends PluginAdapter {
    private String packagePath;
    private String filterColums;
    private Map<InternalType, FullyQualifiedJavaType> types = new HashMap<>();
    
    
    @Override
    public boolean validate(List<String> warnings) {
        this.packagePath = properties.getProperty("packagePath"); //$NON-NLS-1$
        this.filterColums = properties.getProperty("filterColums"); //$NON-NLS-1$
        
        return stringHasValue(this.packagePath) && stringHasValue(this.filterColums);
    }
    /**
     * 生成额外java文件
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        calculateTypeAttributes(this.packagePath, introspectedTable);
        
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
        answer.add(getController(introspectedTable));
        answer.add(getDto(introspectedTable));
        answer.add(getService(introspectedTable));
        answer.add(getServiceImpl(introspectedTable));
        return answer;
    }
    
    private GeneratedJavaFile getController(IntrospectedTable introspectedTable){
        GeneratedController generatedController = new GeneratedController(this.types);
        CompilationUnit controller = generatedController.generated(introspectedTable);
        return  new GeneratedJavaFile(controller, context.getJavaModelGeneratorConfiguration().getTargetProject(), context.getProperty(
            PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), this.context.getJavaFormatter());
    }
    
    private GeneratedJavaFile getDto(IntrospectedTable introspectedTable){
        GeneratedDto generatedDto = new GeneratedDto(this.types, this.filterColums);
        CompilationUnit dto = generatedDto.generated(introspectedTable);
        return  new GeneratedJavaFile(dto, context.getJavaModelGeneratorConfiguration().getTargetProject(), context.getProperty(
            PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), this.context.getJavaFormatter());
    }
    
    private GeneratedJavaFile getService(IntrospectedTable introspectedTable){
        GeneratedService generatedService = new GeneratedService(this.types);
        CompilationUnit service = generatedService.generated(introspectedTable);
        return  new GeneratedJavaFile(service, context.getJavaModelGeneratorConfiguration().getTargetProject(), context.getProperty(
            PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), this.context.getJavaFormatter());
    }
    
    private GeneratedJavaFile getServiceImpl(IntrospectedTable introspectedTable){
        GeneratedServiceImpl generatedServiceImpl = new GeneratedServiceImpl(this.types);
        CompilationUnit serviceImpl = generatedServiceImpl.generated(introspectedTable);
        return  new GeneratedJavaFile(serviceImpl, context.getJavaModelGeneratorConfiguration().getTargetProject(), context.getProperty(
            PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), this.context.getJavaFormatter());
    }
    
   
    protected void calculateTypeAttributes(String basePackage, IntrospectedTable introspectedTable) {
        String name = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        StringBuilder sb = new StringBuilder();
        sb.append(basePackage);
        sb.append(".model.");
        sb.append(name);
        sb.append("Dto");
        types.put(InternalType.ATTR_DTO_TYPE, new FullyQualifiedJavaType(sb.toString()));
        
        sb.setLength(0);
        sb.append(basePackage);
        sb.append(".controller.");
        sb.append(name);
        sb.append("Controller");
        types.put(InternalType.ATTR_CONTROLLER_TYPE, new FullyQualifiedJavaType(sb.toString()));
    
        sb.setLength(0);
        sb.append(basePackage);
        sb.append(".service.");
        sb.append(name);
        sb.append("Service");
        types.put(InternalType.ATTR_SERVICE_TYPE, new FullyQualifiedJavaType(sb.toString()));
        
        sb.setLength(0);
        sb.append(basePackage);
        sb.append(".service.impl.");
        sb.append(name);
        sb.append("ServiceImpl");
        types.put(InternalType.ATTR_SERVICE_IMPL_TYPE, new FullyQualifiedJavaType(sb.toString()));
    
        types.put(InternalType.ATTR_PO_TYPE, new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        types.put(InternalType.ATTR_DAO_TYPE, new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType()));
    
        FullyQualifiedJavaType pageRequestType = new FullyQualifiedJavaType("org.ffm.saas.smarterp.common.model.PageRequest");
        pageRequestType.addTypeArgument(types.get(InternalType.ATTR_DTO_TYPE));
        types.put(InternalType.ATTR_PAGEREQUEST_TYPE, pageRequestType);
        
        FullyQualifiedJavaType pageResponseType = new FullyQualifiedJavaType("org.ffm.saas.smarterp.common.model.PageResponse");
        pageResponseType.addTypeArgument(types.get(InternalType.ATTR_DTO_TYPE));
        types.put(InternalType.ATTR_PAGERESPONSE_TYPE, pageResponseType);
    }
    
    
    
    public static String lowerFirstChar(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
    
    protected enum InternalType {
        ATTR_DTO_TYPE,
        ATTR_CONTROLLER_TYPE,
        ATTR_SERVICE_TYPE,
        ATTR_SERVICE_IMPL_TYPE,
        ATTR_PO_TYPE,
        ATTR_DAO_TYPE,
        ATTR_PAGEREQUEST_TYPE,
        ATTR_PAGERESPONSE_TYPE
    }
}
