package com.qing.tea.entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "email", description = "邮件")
public class Email {
    private static final long serialVersionUID = -2116367492649751914L;

    /**
     * 邮件接收人
     */
    @ApiModelProperty(value = "邮件接收人",example="1609614437@qq.com")
    private String recipient;

    /**
     * 邮件主题
     */
    @ApiModelProperty(value = "邮件主题",example="验证码邮件")
    private String subject;

    /**
     * 邮件内容
     */
    @ApiModelProperty(value = "邮件内容",example="验证码为：548937")
    private String content;
}
