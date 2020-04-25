package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "news")
public class News {
    /*
资讯id：
*/
    @Id
    private String id;


    /*
  资讯题目：
  */
    @Field("title")
    private String title;
    /*
 简介：
 */
    @Field("desc")
    private String desc;
    /*
 内容：
 */
    @Field("content")
    private String content;
    /*
 机构名：
 */
    @Field("name")
    private String name;
    /*
 类型：
 */
    @Field("type")
    private String type;

    /*
作者：
*/
    @Field("writer")
    private String writer;
    /*
 头像：
 */
    @Field("avatar")
    private String avatar;

    /*
封面图片：
*/
    @Field("cover")
    private String cover;
    /*
 跳转链接：
 */
    @Field("href")
    private String href;

    /*
发布者：
*/
    @Field("publisher")
    private String publisher;

    /*
所属机构：
*/
    @Field("org")
    private String org;
    /*
        发布时间
      */
    @Field("date")
    private Date date;
    /*
阅读量：
*/
    @Field("read")
    private int read;

    /*
点赞数：
*/
    @Field("up")
    private int up;

    /*
踩数：
*/
    @Field("down")
    private int down;

    /*
评分：
*/
    @Field("rate")
    private float rate;
    /*
评分人数：
*/
    @Field("rate_num")
    private int rateNum;
    /*
状态：
*/
    @Field("state")
    private String state;
}
