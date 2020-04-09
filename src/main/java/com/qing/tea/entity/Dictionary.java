package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "dictionary")
public class Dictionary {
    /*
    字典id：
    */
    @Id
    private String id;

    /*
    字段code：
    */
    @Field("type_code")
    private String typeCode;

    /*
    字段名：
    */
    @Field("type_name")
    private String typeName;

    /*
    值id：
    */
    @Field("value_id")
    private String valueId;

    /*
    值名：
    */
    @Field("value_name")
    private String valueName;
}
