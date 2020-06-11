package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
@ApiModel(value = "news_detail", description = "用户资讯记录")
@Data
@Document(collection = "news_detail")
public class NewsDetail {
    /*
资讯记录id：
*/
    @Id
    @ApiModelProperty(value = "资讯记录id",example="5e9f0265b23caf3894f49b7f")
    private String id;


    /*
  用户id：
  */
    @Field("user")
    @ApiModelProperty(value = "用户id",example="5e9c77b51fdd831f106b1ba6")
    private ObjectId user;

    public String getUser() {
        if(user!=null){
            return user.toString();
        }else{
            return "";
        }
    }

    public void setUser(String user) {
        if(user!=null)
        this.user =new ObjectId(user) ;
    }
    /*
 资讯id：
 */
    @Field("news")
    @ApiModelProperty(value = "资讯id",example="5e80a353790e9318bc605db4")
    private ObjectId news;

    public String getNews() {
        if(news!=null){
            return news.toString();
        }else{
            return "";
        }
    }

    public void setNews(String news) {
        if(news!=null)
        this.news =new ObjectId(news) ;
    }
    /*
        查看时间
      */
    @Field("date")
    @ApiModelProperty(value = "查看时间",example="2020-05-26 14:54:15")
    private Date date;

    /*
是否点赞：
*/
    @Field("up")
    @ApiModelProperty(value = "是否点赞",example="true")
    private boolean up;

    /*
是否踩：
*/
    @Field("down")
    @ApiModelProperty(value = "是否踩",example="false")
    private boolean down;

    /*
评分：
*/
    @Field("rate")
    @ApiModelProperty(value = "评分",example="3.5")
    private float rate;
    /*
状态：
*/
    @Field("state")
    @ApiModelProperty(value = "状态",example="2")
    private String state;
}
