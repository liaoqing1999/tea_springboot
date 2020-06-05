package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public class Plant {

    /*
        产地
    */
    @Field("place")
    private String place;

    /*
        种植负责人
    */
    @Field("planter")
    private String planter;

    /*
        施药记录
    */
    @Field("pesticide")
    private List<Pesticide> pesticide;

    /*
        图片
    */
    @Field("img")
    private List<String> img;

    /*
状态：
*/
    @Field("finish")
    private boolean finish;
}
