package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
@ApiModel(value = "check", description = "检测阶段")
@Data
public class Check {
    /*
    检测类型
    */
    @Field("type_id")
    @ApiModelProperty(value = "检测类型",example="400003")
    private String typeId;

    /*
    检测日期
  */
    @ApiModelProperty(value = " 检测日期",example="2020-05-26 14:54:15")
    @Field("date")
    private Date date;

    /*
      检测结果
      */
    @ApiModelProperty(value = " 检测结果",example="500002")
    @Field("result")
    private String result;
    /*
          检测详情
          */
    @Field("info")
    @ApiModelProperty(value = "检测详情",example="QmTS7X8wYqz5QDjGHkzcYYDTQtTobounLA95Uie1iSPFgC")
    private String info;
    /*
      检测负责人
      */
    @Field("checker")
    @ApiModelProperty(value = "检测负责人",example="5eae61ff27eee24a7100f007")
    private String checker;

    /*
状态：
*/
    @Field("finish")
    private boolean finish;
}
