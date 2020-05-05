package com.qing.tea.utils;

import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;

public class UpdateUtils {
    public static Update getUpdate(Object obj){
        Update update = new Update();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            String key = field.getName();
            boolean accessFlag = field.isAccessible();
            // 修改访问控制权限
            field.setAccessible(true);
            Object value = new Object();
            try {
                value = field.get(obj);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(accessFlag);
            update.set(key, value);
        }
        return update;
    }
}
