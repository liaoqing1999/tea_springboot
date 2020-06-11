package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@ApiModel(value = "produce", description = "产品")
@Data
@Document(collection = "produce")
public class Produce {
    /*
   产品id：
   */
    @Id
    @ApiModelProperty(value = "产品id",example="5e8f2cf800935353f84e1391")
    private String id;
    /*
    产品名
    */
    @Field("name")
    @ApiModelProperty(value = "产品名",example="龙井")
    private String name;
    /*
    产品类型
     */
    @Field("type_id")
    @ApiModelProperty(value = "产品类型",example="001002")
    private String typeId;

    /*
        产品描述
    */
    @Field("desc")
    @ApiModelProperty(value = "产品描述",example="极品龙井，美味清香")
    private String desc;
    /*
        产品等级
    */
    @Field("grade")
    @ApiModelProperty(value = "产品等级",example="100002")
    private String grade;

    /*
        产品规格
    */
    @Field("specs")
    @ApiModelProperty(value = "产品规格",example="150g/盒")
    private String specs;
    /*
        食用方法
    */
    @Field("eat")
    @ApiModelProperty(value = "食用方法",example="取10克左右茶叶加少量沸水润茶后再加入适量沸水冲泡，即可饮用")
    private String eat;
    /*
         价格
    */
    @Field("price")
    @ApiModelProperty(value = "价格",example="201.6")
    private double price;
    /*
        图片地址
    */
    @Field("img")
    @ApiModelProperty(value = "图片地址")
    private String[] img;
    /*
        机构
    */
    @Field("org")
    @ApiModelProperty(value = "机构",example="5e8c86dbd49c4d68bcb7b635")
    private ObjectId org;

    public String getOrg() {
        if(org!=null){
            return org.toString();
        }else{
            return "";
        }
    }

    public void setOrg(String org) {
        if(org!=null)
        this.org =new ObjectId(org) ;
    }
    /*
        存量
    */
    @Field("reserve")
    @ApiModelProperty(value = "存量",example="499")
    private int reserve;
    /*
状态：
*/
    @Field("state")
    @ApiModelProperty(value = "状态",example="2")
    private String state;
}
