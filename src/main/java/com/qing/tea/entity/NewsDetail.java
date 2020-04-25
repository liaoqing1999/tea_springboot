package com.qing.tea.entity;

import lombok.Data;
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
    @Field("user_id")
    private String userId;
    /*
 资讯id：
 */
    @Field("news_id")
    private String newsId;
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
