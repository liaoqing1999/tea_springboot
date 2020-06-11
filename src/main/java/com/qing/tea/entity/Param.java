package com.qing.tea.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "param", description = "合并参数")
public class Param {
    @ApiModelProperty(value = "机构")
    public Org org;
    @ApiModelProperty(value = "管理员")
    public Staff staff;
}
