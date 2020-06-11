package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
@ApiModel(value = "role", description = "角色")
@Data
@Document(collection = "role")
public class Role {
    /*
角色id：
*/
    @Id
    @ApiModelProperty(value = "角色id",example="5e92f60b00935353f84e1392")
    private String id;

    /*
角色名：
*/
    @Field("name")
    @ApiModelProperty(value = "角色名",example="plant")
    private String name;
    /*
权限菜单：
*/
    @Field("menu")
    @ApiModelProperty(value = "权限菜单")
    private String[] menu;
    /*
   创建时间
 */
    @Field("create_time")
    @ApiModelProperty(value = " 创建时间",example="2020-05-26 14:54:15")
    private Date createTime;

    /*
授权时间
*/
    @Field("auth_time")
    @ApiModelProperty(value = " 授权时间",example="2020-05-26 14:54:15")
    private Date authTime;

    /*
授权人名：
*/
    @Field("auth_name")
    @ApiModelProperty(value = " 授权人名",example="admin")
    private String authName;
}
