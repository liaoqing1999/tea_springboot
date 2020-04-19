package com.qing.tea.controller;

import java.util.HashMap;
import java.util.Map;

public class MapReuslt {
    public static Map<String, Object> mapPage(Object content, long total, int page, int rows){
        Map<String,Object> result =new HashMap<String,Object>();
        result.put("content",content);
        result.put("total", total);
        result.put("page",page);
        result.put("rows",rows);
        return result;
    }
}
