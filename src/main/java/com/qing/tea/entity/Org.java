package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@ApiModel(value = "org", description = "机构")
@Data
@Document(collection = "org")
public class Org {
    /*
   机构id：
   */
    @Id
    @ApiModelProperty(value = "机构id",example="5e8ac3a800935353f84e1390")
    private String id;

    /*
  机构名：
  */
    @Field("name")
    @ApiModelProperty(value = "机构名",example="长沙新茶叶公司")
    private String name;

    /*
 机构法人：
 */
    @Field("corporation")
    @ApiModelProperty(value = "机构法人",example="法人1")
    private String corporation;
    /*
 机构图标：
 */
    @Field("trademark")
    @ApiModelProperty(value = "机构图标",example="QmaABmov2yMv4tHTToy7sRL6VVqjHp6DEeUnThSQNEMy5y")
    private String trademark;
    /*
机构电话：
*/
    @Field("phone")
    @ApiModelProperty(value = "机构电话",example="15674444444")
    private String phone;

    /*
机构描述信息：
*/
    @Field("description")
    @ApiModelProperty(value = "机构描述信息",example="湖南新茶叶公司是新成立的一家茶叶公司")
    private String description;

    /*
机构生产许可证编号：
*/
    @Field("license")
    @ApiModelProperty(value = "机构生产许可证编号",example="SC12233090302656")
    private String license;

    /*
营业执照：
*/
    @Field("permit")
    @ApiModelProperty(value = "营业执照")
    private String[] permit;

    /*
组织机构代码证：
*/
    @Field("codePermit")
    @ApiModelProperty(value = "组织机构代码证")
    private String[] codePermit;

    /*
邮箱：
*/
    @Field("email")
    @ApiModelProperty(value = "邮箱",example="24442@QQ.COM")
    private String email;

    /*
地点：
*/
    @Field("place")
    @ApiModelProperty(value = "地点",example="四川-成都市-市辖区")
    private String place;

    /*
备注：
*/
    @Field("remark")
    @ApiModelProperty(value = "备注",example="备注测试")
    private String remark;
    /*
是否允许非管理员新建产品：
*/
    @Field("staff_produce")
    @ApiModelProperty(value = "是否允许非管理员新建产品",example="true")
    private boolean staffProduce;
    /*
状态：
*/
    @Field("state")
    @ApiModelProperty(value = "状态",example="2")
    private String state;
}