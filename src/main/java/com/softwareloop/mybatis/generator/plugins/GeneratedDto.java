package com.softwareloop.mybatis.generator.plugins;

import com.softwareloop.mybatis.generator.plugins.GeneratedCrudPlugin.InternalType;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class GeneratedDto {
    private Map<InternalType, FullyQualifiedJavaType> types;
    private String filterColums;
    
    public GeneratedDto(Map<InternalType, FullyQualifiedJavaType> types, String filterColums){
        this.types = types;
        this.filterColums = filterColums;
    }
  
    public CompilationUnit generated(IntrospectedTable introspectedTable) {
        String domainObjectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        
        TopLevelClass dto = new TopLevelClass(types.get(InternalType.ATTR_DTO_TYPE));
        dto.setVisibility(JavaVisibility.PUBLIC);
        
        Iterator<IntrospectedColumn> var9 = filterField(introspectedTable.getAllColumns()).iterator();
        //添加序列化
        addSerializable(dto);
        //添加lombok
        dto.addImportedType("lombok.Data");
        dto.addImportedType("lombok.NoArgsConstructor");
        dto.addAnnotation("@Data");
        dto.addAnnotation("@NoArgsConstructor");
        
        //swagger注释.
        dto.addAnnotation("@ApiModel(\"" + introspectedTable.getRemarks() + "\")");
        dto.addImportedType("io.swagger.annotations.ApiModel");
        dto.addImportedType("io.swagger.annotations.ApiModelProperty");
        
        
        while (var9.hasNext()) {
            IntrospectedColumn introspectedColumn = var9.next();
            Field field = getField(introspectedColumn);
            dto.addField(field);
            dto.addImportedType(field.getType());
            
            field.addAnnotation("@ApiModelProperty(\"" + introspectedColumn.getRemarks() + "\")");
//            if (addSwagger) {
//                field.addAnnotation("@ApiModelProperty(\"" + introspectedColumn.getRemarks() + "\")");
//            } else if (stringHasValue(introspectedColumn.getRemarks())) {
//                field.addJavaDocLine("/**");
//                field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
//                field.addJavaDocLine(" */");
//            }
        }
        return dto;
    }
    
    private List<IntrospectedColumn> filterField(List<IntrospectedColumn> columns){
        final List<String> filters = Arrays.asList(this.filterColums.split(","));
        return columns.stream().filter(p ->{
            return !filters.contains(p.getActualColumnName());
        }).collect(Collectors.toList());
    }
    
    
    /**
     * 获取field
     *
     * @param introspectedColumn
     * @return
     */
    private Field getField(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(fqjt);
        field.setName(property);
        return field;
    }
    
    /**
     * 添加序列化
     *
     * @param dto
     */
    private void addSerializable(TopLevelClass dto) {
        //序列化
        dto.addImportedType(new FullyQualifiedJavaType("java.io.Serializable"));
        dto.addSuperInterface(new FullyQualifiedJavaType("java.io.Serializable"));
        Field field1 = new Field();
        field1.setFinal(true);
        field1.setInitializationString("1L");
        field1.setName("serialVersionUID");
        field1.setStatic(true);
        field1.setType(new FullyQualifiedJavaType("long"));
        field1.setVisibility(JavaVisibility.PRIVATE);
        dto.addField(field1);
    }
    
   

}
