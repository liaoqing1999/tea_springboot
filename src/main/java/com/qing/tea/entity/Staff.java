package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
@ApiModel(value = "staff", description = "用户")
@Data
@Document(collection = "staff")
public class Staff {
    /*
用户id：
*/
    @Id
    @ApiModelProperty(value = "用户id",example="5e95a45700935353f84e1394")
    private String id;
    /*
用户名：
*/
    @Field("name")
    @ApiModelProperty(value = "用户名",example="plant")
    private String name;

    /*
邮箱：
*/
    @Field("email")
    @ApiModelProperty(value = "邮箱",example="1609614437@qq.com")
    private String email;
    /*
用户密码：
*/
    @Field("password")
    @ApiModelProperty(value = "用户密码",example="123456")
    private String password;

    /*
用户真实姓名：
*/
    @Field("realName")
    @ApiModelProperty(value = "用户真实姓名",example="张三")
    private String realName;

    /*
性别：
*/
    @Field("sex")
    @ApiModelProperty(value = "用户真实姓名",example="1")
    private String sex;

    /*
手机号码：
*/
    @Field("phone")
    @ApiModelProperty(value = "手机号码",example="15674444444")
    private String phone;

    /*
身份证号码：
*/
    @Field("card")
    @ApiModelProperty(value = "身份证号码",example="511024199010054022")
    private String card;
    /*
工作：
*/
    @Field("work")
    @ApiModelProperty(value = "工作",example="老师")
    private String work;

    /*
    机构id：
    */
    @Field("org")
    @ApiModelProperty(value = "机构id",example="5e8c86dbd49c4d68bcb7b635")
    private ObjectId org;

    public String getOrg() {
        if(org!=null){
            return org.toString();
        }else{
            return null;
        }
    }

    public void setOrg(String org) {
        if(org!=null)  this.org =new ObjectId(org) ;
    }
    /*
头像图片：
*/
    @Field("img")
    @ApiModelProperty(value = "头像图片",example="Qmafb4bRbR69WsCh6FZCtE2zPg4z4nsJ2WhMJLLpcFpKnn")
    private String img;

    /*
状态：
*/
    @Field("state")
    @ApiModelProperty(value = "状态",example="2")
    private String state;

    /*
创建时间：
*/
    @Field("createTime")
    @ApiModelProperty(value = "创建时间",example="2020-05-26 14:54:15")
    private Date createTime;

    /*
角色：
*/
    @Field("role")
    @ApiModelProperty(value = "角色",example="5e92f60b00935353f84e1392")
    private ObjectId role;


    public String getRole() {
        if(role!=null){
            return role.toString();
        }else {
            return null;
        }
    }
    public void setRole(String role) {
        if(role!=null)
        this.role = new ObjectId(role);
    }
}
