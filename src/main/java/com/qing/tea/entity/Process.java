package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class Process {
    /*
    加工方法
    */
    @Field("method")
    private String method;

    /*
    开始日期
  */
    @Field("startDate")
    private Date startDate;

    /*
      结束日期
      */
    @Field("endDate")
    private Date endDate;

    /*
      加工负责人
      */
    @Field("processer")
    private String processer;
}
