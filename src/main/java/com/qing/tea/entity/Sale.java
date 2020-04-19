package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class Sale {
    /*
     售卖地点
     */
    @Field("place")
    private String place;

    /*
    售卖日期
  */
    @Field("date")
    private Date date;

    /*
      售卖方式
      */
    @Field("method")
    private String method;

    /*
      售卖人
      */
    @Field("saleer")
    private String saleer;

    /*
状态：
*/
    @Field("isFinish")
    private Boolean isFinish;
}
