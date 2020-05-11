package com.qing.tea.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "news_detail")
public class NewsDetail {
    /*
资讯记录id：
*/
    @Id
    private String id;


    /*
  用户id：
  */
    @Field("user")
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
    private Date date;

    /*
是否点赞：
*/
    @Field("up")
    private boolean up;

    /*
是否踩：
*/
    @Field("down")
    private boolean down;

    /*
评分：
*/
    @Field("rate")
    private float rate;
    /*
状态：
*/
    @Field("state")
    private String state;
}
