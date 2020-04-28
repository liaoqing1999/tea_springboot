package com.qing.tea.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "org")
public class Org {
    /*
   机构id：
   */
    @Id
    private String id;

    /*
  机构名：
  */
    @Field("name")
    private String name;

    /*
 机构法人：
 */
    @Field("corporation")
    private String corporation;
    /*
 机构图标：
 */
    @Field("trademark")
    private String trademark;
    /*
机构电话：
*/
    @Field("phone")
    private String phone;

    /*
机构描述信息：
*/
    @Field("description")
    private String description;

    /*
机构生产许可证编号：
*/
    @Field("license")
    private String license;

    /*
营业执照：
*/
    @Field("permit")
    private String[] permit;

    /*
组织机构代码证：
*/
    @Field("codePermit")
    private String[] codePermit;

    /*
邮箱：
*/
    @Field("email")
    private String email;

    /*
地点：
*/
    @Field("place")
    private String place;

    /*
地点：
*/
    @Field("remark")
    private String remark;

    /*
   产地：
*/
    @Field("place_origin")
    private String[] placeOrigin;
    /*
仓库：
*/
    @Field("warehouse")
    private String[] warehouse;
    /*
管理员id：
*/
    @Field("admin")
    private String admin;

    /*
产品：
*/
    @Field("produce")
    private String[] produce;
    /*
是否允许非管理员新建产品：
*/
    @Field("staff_produce")
    private boolean staffProduce;
    /*
状态：
*/
    @Field("state")
    private String state;
}