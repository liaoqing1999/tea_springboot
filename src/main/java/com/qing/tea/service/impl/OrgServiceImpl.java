package com.qing.tea.service.impl;
import com.qing.tea.entity.Org;
import com.qing.tea.service.OrgService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class OrgServiceImpl implements OrgService {
    @Resource
    private MongoTemplate mongoTemplate;


    @Override
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), Org.class);
    }

    @Override
    public Org insert(Org org) {
        mongoTemplate.insert(org);
        return org;
    }

    @Override
    public void delete(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Org.class);
    }

    @Override
    public void update(String id, String name, Object value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, Org.class);
    }
    @Override
    public void update(Org org) {
        Query query=new Query(Criteria.where("_id").is(org.getId()));
        Update update = new Update();
        Field[] declaredFields = org.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            String key = field.getName();
            boolean accessFlag = field.isAccessible();
            // 修改访问控制权限
            field.setAccessible(true);
            Object value = new Object();
            try {
                value = field.get(org);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(accessFlag);
            update.set(key, value);
        }
        mongoTemplate.updateFirst(query, update, Org.class);
    }

    @Override
    public Org find(String id) {
        return mongoTemplate.findById(id,Org.class);
    }

    @Override
    public List<Org> findAll() {
        return mongoTemplate.findAll(Org.class);
    }

    @Override
    public List<Org> findByCond(Criteria criteria) {
        Query query=new Query(criteria);
        return mongoTemplate.find(query, Org.class);
    }

    @Override
    public List<Org> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where(name).regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Org.class);
    }

    @Override
    public List<Org> findList(int page, int rows, Criteria criteria) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        Query query = new Query(criteria);
        query.skip((page-1)*rows).limit(rows);
        return mongoTemplate.find(query, Org.class);
    }
}
