package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
@ApiModel(value = "storage", description = "仓储阶段")
@Data
public class Storage {
    /*
   仓储地点
   */
    @Field("place")
    @ApiModelProperty(value = "仓储地点",example="长沙茶叶仓库")
    private String place;

    /*
    开始日期
  */
    @Field("start_date")
    @ApiModelProperty(value = " 开始日期",example="2020-05-26 14:54:15")
    private Date startDate;

    /*
      结束日期
      */
    @Field("end_date")
    @ApiModelProperty(value = " 结束日期",example="2020-05-27 14:54:15")
    private Date endDate;

    /*
      仓储负责人
      */
    @Field("storageer")
    @ApiModelProperty(value = " 仓储负责人",example="5ead91473f12fd4b8bdb7f7f")
    private String storageer;

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
