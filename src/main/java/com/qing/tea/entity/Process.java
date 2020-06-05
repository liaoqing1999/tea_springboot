package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

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
    @Field("start_date")
    private Date startDate;

    /*
      结束日期
      */
    @Field("end_date")
    private Date endDate;

    /*
      加工负责人
      */
    @Field("processer")
    private String processer;

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
