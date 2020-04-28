package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class Check {
    /*
    检测类型
    */
    @Field("type_id")
    private String typeId;

    /*
    检测日期
  */
    @Field("date")
    private Date date;

    /*
      检测结果
      */
    @Field("result")
    private String result;
    /*
          检测详情
          */
    @Field("info")
    private String info;
    /*
      检测负责人
      */
    @Field("checker")
    private String checker;

    /*
状态：
*/
    @Field("finish")
    private boolean finish;
}
