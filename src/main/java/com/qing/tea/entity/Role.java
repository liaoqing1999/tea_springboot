package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "role")
public class Role {
    /*
角色id：
*/
    @Id
    private String id;

    /*
角色名：
*/
    @Field("name")
    private String name;
    /*
角色名：
*/
    @Field("menu")
    private String[] menu;
    /*
   创建时间
 */
    @Field("create_time")
    private Date createTime;

    /*
授权时间
*/
    @Field("auth_time")
    private Date authTime;

    /*
授权人名：
*/
    @Field("auth_name")
    private String authName;
}
