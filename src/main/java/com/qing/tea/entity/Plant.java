package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
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
    private Pesticide[] pesticide;

    /*
        图片
    */
    @Field("img")
    private String[] img;
}
