package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
@ApiModel(value = "pesticide", description = "施药记录")
@Data
public class Pesticide {
    /*
    农药名
    */
    @Field("name")
    @ApiModelProperty(value = "农药名",example="200001")
    private String name;
    /*
        施药时间
      */
    @Field("date")
    @ApiModelProperty(value = "施药时间",example="2020-05-26 14:54:15")
    private Date date;
}
