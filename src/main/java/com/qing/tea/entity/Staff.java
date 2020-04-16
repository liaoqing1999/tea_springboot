package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "staff")
public class Staff {
    /*
用户id：
*/
    @Id
    private String id;
    /*
用户名：
*/
    @Field("name")
    private String name;
    /*
邮箱：
*/
    @Field("email")
    private String email;
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

    /*
头像图片：
*/
    @Field("img")
    private String img;

    /*
状态：
*/
    @Field("state")
    private String state;

    /*
创建时间：
*/
    @Field("create_time")
    private Date createTime;

    /*
角色：
*/
    @Field("role")
    private String role;
}
