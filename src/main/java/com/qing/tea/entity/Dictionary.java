package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@ApiModel(value = "dictionary", description = "字典")
@Data
@Document(collection = "dictionary")
public class Dictionary {
    /*
    字典id：
    */
    @Id
    @ApiModelProperty(value = "字典id",example="5e7f6e5b790e9318bc605d81")
    private String id;

    /*
    字段code：
    */
    @ApiModelProperty(value = "字段code",example="type")
    @Field("type_code")
    private String typeCode;

    /*
    字段名：
    */
    @ApiModelProperty(value = "字段名",example="茶叶类型")
    @Field("type_name")
    private String typeName;

    /*
    值id：
    */
    @ApiModelProperty(value = "值id",example="001000")
    @Field("value_id")
    private String valueId;

    /*
    值名：
    */
    @ApiModelProperty(value = "值名",example="绿茶")
    @Field("value_name")
    private String valueName;

    /*
状态：
*/
    @ApiModelProperty(value = "状态",example="2")
    @Field("state")
    private String state;
}
