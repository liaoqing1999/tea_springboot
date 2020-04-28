package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Data
@Document(collection = "produce")
public class Produce {
    /*
   产品id：
   */
    @Id
    private String id;
    /*
    产品名
    */
    @Field("name")
    private String name;
    /*
    产品类型
     */
    @Field("type_id")
    private String typeId;

    /*
        产品描述
    */
    @Field("des")
    private String des;
    /*
        产品等级
    */
    @Field("grade")
    private String grade;
    /*
        食用方法
    */
    @Field("eat")
    private String eat;
    /*
         价格
    */
    @Field("price")
    private String price;
    /*
        图片地址
    */
    @Field("img")
    private String[] img;
    /*
        机构
    */
    @Field("org")
    private String org;
    /*
        存量
    */
    @Field("reserve")
    private String reserve;
    /*
状态：
*/
    @Field("state")
    private String state;
}
