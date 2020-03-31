package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class Pesticide {
    /*
    农药名
    */
    @Field("name")
    private String name;
    /*
        施药时间
      */
    @Field("date")
    private Date date;
}
