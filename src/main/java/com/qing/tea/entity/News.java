package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
@ApiModel(value = "news", description = "资讯")
@Data
@Document(collection = "news")
public class News {
    /*
资讯id：
*/
    @Id
    @ApiModelProperty(value = "资讯id",example="5e9c77b51fdd831f106b1ba6")
    private String id;


    /*
  资讯题目：
  */
    @Field("title")
    @ApiModelProperty(value = "资讯题目",example="平台资讯简介标题")
    private String title;
    /*
 简介：
 */
    @Field("desc")
    @ApiModelProperty(value = "简介",example="这里是平台简介资讯描述，用于简单介绍")
    private String desc;
    /*
 内容：
 */
    @Field("content")
    @ApiModelProperty(value = "简介",example="<p>这里是</p><p>平台简介</p>内容")
    private String content;
    /*
 类型：
 */
    @Field("type")
    @ApiModelProperty(value = "类型",example="1")
    private String type;

    /*
作者：
*/
    @Field("writer")
    @ApiModelProperty(value = "作者",example="admin")
    private String writer;
    /*
 头像：
 */
    @Field("avatar")
    @ApiModelProperty(value = "头像",example="Qmafb4bRbR69WsCh6FZCtE2zPg4z4nsJ2WhMJLLpcFpKnn")
    private String avatar;

    /*
封面图片：
*/
    @Field("cover")
    @ApiModelProperty(value = "封面图片",example="QmSF8XuApf6R1LKyA5d7WjizZXBXLSnfZ8UtV8Fipxt5rX")
    private String cover;
    /*
 跳转链接：
 */
    @ApiModelProperty(value = "跳转链接",example="http://www.puercn.com/chayenews/cyzx/210117.html")
    @Field("href")
    private String href;

    /*
发布者：
*/
    @ApiModelProperty(value = "发布者",example="admin")
    @Field("publisher")
    private String publisher;

    /*
所属机构：
*/
    @ApiModelProperty(value = "所属机构",example="5e8c86dbd49c4d68bcb7b635")
    @Field("org")
    private String org;

    /*
所属机构名：
*/
    @ApiModelProperty(value = "所属机构名",example="吉祥牌")
    @Field("orgName")
    private String orgName;
    /*
        发布时间
      */
    @ApiModelProperty(value = "发布时间",example="2020-04-19 16:00:00")
    @Field("date")
    private Date date;
    /*
阅读量：
*/
    @ApiModelProperty(value = "阅读量",example="25")
    @Field("read")
    private int read;

    /*
点赞数：
*/
    @ApiModelProperty(value = "点赞数",example="11")
    @Field("up")
    private int up;

    /*
踩数：
*/
    @ApiModelProperty(value = "踩数",example="5")
    @Field("down")
    private int down;

    /*
评分：
*/
    @ApiModelProperty(value = "评分",example="3.5")
    @Field("rate")
    private double rate;
    /*
评分人数：
*/
    @ApiModelProperty(value = "评分人数",example="11")
    @Field("rate_num")
    private int rateNum;
    /*
状态：
*/
    @ApiModelProperty(value = "状态",example="2")
    @Field("state")
    private String state;
}
