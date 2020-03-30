package com.qing.tea.utils;



/**
 * @author xyl
 * @since 2020-03-06 12:50:29
 */

public enum ResultCode {
    SUCCESS("操作成功",200),
    FAILED("操作失败",500),
    VALIDATE_FAILED("参数检验失败",400),
    NOTFOUND("找不到资源",404),
    FORBIDDEN("没有相关权限",403),
    UNAUTHORIZED("暂未登录或token已经过期",401);

    private String message;

    private Integer code;


    ResultCode(ResultCode result,Object data){
        this.message = result.message;
        this.code = result.code;
    }

    ResultCode(String message, Integer code){
        this.message = message;
        this.code = code;
    }

    public String getMessage(){
        return message;
    }

    public Integer getCode(){
        return code;
    }



}

