package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "staff")
public class Staff {
    /*
用户id：
*/
    @Field("_id")
    private String id;
    /*
用户名：
*/
    @Field("name")
    private String name;

    /*
用户密码：
*/
    @Field("password")
    private String password;

    /*
用户密码：
*/
    @Field("real_name")
    private String realName;

    /*
手机号码：
*/
    @Field("phone")
    private String phone;

    /*
身份证号码：
*/
    @Field("card")
    private String card;
    /*
工作：
*/
    @Field("work")
    private String work;
    /*
机构id：
*/
    @Field("org")
    private String org;


}
