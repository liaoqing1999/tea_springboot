package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
@ApiModel(value = "process", description = "加工阶段")
@Data
public class Process {
    /*
    加工方法
    */
    @Field("method")
    @ApiModelProperty(value = "加工方法",example="300001")
    private String method;

    /*
    开始日期
  */
    @Field("start_date")
    @ApiModelProperty(value = "开始日期",example="2020-05-26 14:54:15")
    private Date startDate;

    /*
      结束日期
      */
    @Field("end_date")
    @ApiModelProperty(value = "结束日期",example="2020-05-27 14:54:15")
    private Date endDate;

    /*
      加工负责人
      */
    @Field("processer")
    @ApiModelProperty(value = "加工负责人",example="5ead90a23f12fd4b8bdb7f7e")
    private String processer;

    /*
        图片
    */
    @Field("img")
    @ApiModelProperty(value = "图片")
    private List<String> img;

    /*
状态：
*/
    @Field("finish")
    @ApiModelProperty(value = "状态",example="true")
    private boolean finish;
}
