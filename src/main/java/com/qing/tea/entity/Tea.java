package com.qing.tea.entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.util.List;

@Data
@Document(collection = "tea")
public class Tea implements Serializable {
    /*
    茶叶id：
    */
    @Id
    private String id;
    /*
    茶名
    */
    @Field("name")
    private String name;
    /*
    类型
     */
    @Field("type_id")
    private String typeId;

    /*
        批次
    */
    @Field("batch")
    private String batch;
    /*
        产品等级
    */
    @Field("grade")
    private String grade;
    /*
        保质期
    */
    @Field("period")
    private String period;
    /*
         存储条件
    */
    @Field("store")
    private String store;
    /*
        图片地址
    */
    @Field("img")
    private String[] img;
    /*
        二维码地址
    */
    @Field("qr")
    private String qr;
    /*
        种植阶段
    */
    @Field("plant")
    private Plant plant;

    /*
        加工阶段
    */
    @Field("process")
    private List<Process> process;

    /*
        是否完成加工阶段
    */
    @Field("process_finish")
    private boolean processFinish;

    /*
        仓储阶段
    */
    @Field("storage")
    private Storage storage;

    /*
        检测阶段
    */
    @Field("check")
    private List<Check> check;

    /*
       是否完成检测阶段
   */
    @Field("check_finish")
    private boolean checkFinish;
    /*
       所属产品
  */
    @Field("produce")
    private String produce;
}
