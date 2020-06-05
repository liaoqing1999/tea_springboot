package com.qing.tea.service.impl;

import com.qing.tea.entity.Org;
import com.qing.tea.entity.Staff;
import com.qing.tea.entity.Tea;
import com.qing.tea.service.TeaService;
import com.qing.tea.utils.UpdateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class TeaServiceImpl implements TeaService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Tea insert(Tea tea) {
        mongoTemplate.insert(tea);
        return tea;
    }

    @Override
    public void delete(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Tea.class);
    }

    @Override
    public void update(String id,String name,Object value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, Tea.class);
    }

    @Override
    public void update(Tea tea) {
        Query query=new Query(Criteria.where("_id").is(tea.getId()));
        Update update = UpdateUtils.getUpdate(tea);
        mongoTemplate.updateFirst(query, update, Tea.class);
    }

    @Override
    public Tea find(String id) {
        return mongoTemplate.findById(id,Tea.class);
    }

    @Override
    public List<Tea> findAll() {
        return mongoTemplate.findAll(Tea.class);
    }

    @Override
    public List<Tea> findByCond(Criteria criteria) {
        Query query=new Query(criteria);
        return mongoTemplate.find(query, Tea.class);
    }

    @Override
    public List<Tea> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where(name).regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Tea.class);
    }

    @Override
    public List<Tea> findList(int page, int rows,Criteria criteria) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        Query query = new Query(criteria);
        query.skip((page-1)*rows).limit(rows);
        return mongoTemplate.find(query.with(Sort.by("batch")), Tea.class);
    }

    @Override
    public List<Object> findListStaff(int page, int rows, Criteria criteria) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        Query query = new Query(criteria);
        query.skip((page-1)*rows).limit(rows);
        List<Tea> teaList = mongoTemplate.find(query.with(Sort.by("batch")), Tea.class);
        List<Object> results = new ArrayList<Object>();
        for(Tea tea:teaList){
            Map<String,Object> map = new HashMap<String, Object>();
            try {
                map =  objectToMap(tea);
                try{
                    HashMap<String, Object> plant =(HashMap<String, Object>)map.get("plant");
                    map.put("plant",getStaff(plant,"planter"));

                    ArrayList<HashMap<String, Object>> process =(ArrayList<HashMap<String, Object>>)map.get("process");
                    if(null!=process) {
                        for (int i = 0; i < process.size(); i++) {
                            HashMap<String, Object> p = process.get(i);
                            process.set(i, getStaff(p, "processer"));
                        }
                    }
                    map.put("process",process);

                    HashMap<String, Object> storage =(HashMap<String, Object>)map.get("storage");
                    map.put("storage",getStaff(storage,"storageer"));

                    ArrayList<HashMap<String, Object>> check =(ArrayList<HashMap<String, Object>>)map.get("check");
                    if(null!=check){
                        for(int i=0;i<check.size();i++){
                            HashMap<String, Object> p = check.get(i);
                            check.set(i, getStaff(p,"checker"));
                        }
                    }

                    map.put("check",check);
                    results.add(map);
                }catch(ClassCastException e){

                }
            } catch (IllegalAccessException e) {
                results.add(tea);
            }
        }
        return results;
    }

    public HashMap<String, Object> getStaff(HashMap<String, Object> obj,String str){
        if(null!=obj){
            String id =(String) obj.get(str);
            if(null!=id){
                Staff staff = mongoTemplate.findById(id, Staff.class);
                if(null!=staff){
                    obj.put("staffName",staff.getName());
                    obj.put("staffRealName",staff.getRealName());
                }
            }
        }
        return  obj;
    }

    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String,Object>();
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
            if(null!=value){
                Class<?> aClass = value.getClass();
                if(isPrimitive(value)||aClass.getName().contains("java.lang.String")||aClass.getName().contains("Date")){

                }else if(aClass.getName().contains("java.util.ArrayList")){
                    try{
                        ArrayList v= (ArrayList)value;
                        for(int i=0;i<v.size();i++){
                            if(isPrimitive(v.get(i))||v.get(i).getClass().getName().contains("java.lang.String")||v.get(i).getClass().getName().contains("Date")){

                            }else{
                                v.set(i,objectToMap(v.get(i)));
                            }
                        }
                        value = v;
                    }catch(ClassCastException e){

                    }
                }else{
                    value = objectToMap(value);
                }
            }
            map.put(key, value);
        }
        return map;
    }

    private static boolean isPrimitive(Object obj) {
        try {
            return ((Class<?>)obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), Tea.class);
    }

}
