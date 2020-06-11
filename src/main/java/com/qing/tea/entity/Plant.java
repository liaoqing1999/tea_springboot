package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
@ApiModel(value = "plant", description = "种植阶段")
@Data
public class Plant {

    /*
        产地
    */
    @Field("place")
    @ApiModelProperty(value = "产地",example="湖南长沙茶叶生产基地")
    private String place;

    /*
        种植负责人
    */
    @Field("planter")
    @ApiModelProperty(value = "产地",example="5e95a45700935353f84e1394")
    private String planter;

    /*
        施药记录
    */
    @Field("pesticide")
    @ApiModelProperty(value = "施药记录")
    private List<Pesticide> pesticide;

    /*
        图片
    */
    @Field("img")
    @ApiModelProperty(value = "图片")
    private List<String> img;

    /*
是否完成：
*/
    @Field("finish")
    @ApiModelProperty(value = "是否完成",example="true")
    private boolean finish;
}
