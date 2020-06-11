package com.qing.tea.entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.util.List;
@ApiModel(value = "tea", description = "茶叶")
@Data
@Document(collection = "tea")
public class Tea implements Serializable {
    /*
    茶叶id：
    */
    @Id
    @ApiModelProperty(value = "茶叶id",example="5e7f52b6790e9318bc605d80")
    private String id;
    /*
    茶名
    */
    @Field("name")
    @ApiModelProperty(value = "茶名",example="白毫银针")
    private String name;
    /*
    类型
     */
    @Field("type_id")
    @ApiModelProperty(value = "类型",example="002001")
    private String typeId;

    /*
        批次
    */
    @Field("batch")
    @ApiModelProperty(value = "批次",example="20200329")
    private String batch;
    /*
        产品等级
    */
    @Field("grade")
    @ApiModelProperty(value = "产品等级",example="100001")
    private String grade;
    /*
        保质期
    */
    @Field("period")
    @ApiModelProperty(value = "保质期",example="12个月")
    private String period;
    /*
         存储条件
    */
    @Field("store")
    @ApiModelProperty(value = "存储条件",example="干燥通风")
    private String store;
    /*
        图片地址
    */
    @Field("img")
    @ApiModelProperty(value = "图片地址")
    private List<String> img;
    /*
        二维码地址
    */
    @Field("qr")
    @ApiModelProperty(value = "二维码地址",example = "QmepUgdVg92QVmwRnYiVWdfYUjXjU181hBM4p9seTjKsHf")
    private String qr;
    /*
        种植阶段
    */
    @Field("plant")
    @ApiModelProperty(value = "种植阶段")
    private Plant plant;

    /*
        加工阶段
    */
    @Field("process")
    @ApiModelProperty(value = "加工阶段")
    private List<Process> process;

    /*
        是否完成加工阶段
    */
    @Field("process_finish")
    @ApiModelProperty(value = "是否完成加工阶段",example = "true")
    private boolean processFinish;

    /*
        仓储阶段
    */
    @Field("storage")
    @ApiModelProperty(value = "仓储阶段")
    private Storage storage;

    /*
        检测阶段
    */
    @Field("check")
    @ApiModelProperty(value = "检测阶段")
    private List<Check> check;

    /*
       是否完成检测阶段
   */
    @Field("check_finish")
    @ApiModelProperty(value = "是否完成检测阶段",example = "true")
    private boolean checkFinish;
    /*
       所属产品
  */
    @Field("produce")
    @ApiModelProperty(value = "所属产品",example = "5e8f2cf800935353f84e1391")
    private String produce;
}
