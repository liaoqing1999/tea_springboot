package com.qing.tea.controller;

import java.util.HashMap;
import java.util.Map;

public class MapReuslt {
    /**
     * 获取分页返回对象
     * @param content 分页内容
     * @param total 总数
     * @param page 页数
     * @param rows 行数
     * @return
     */
    public static Map<String, Object> mapPage(Object content, long total, int page, int rows){
        Map<String,Object> result =new HashMap<String,Object>();
        result.put("content",content);
        result.put("total", total);
        result.put("page",page);
        result.put("rows",rows);
        return result;
    }
}
