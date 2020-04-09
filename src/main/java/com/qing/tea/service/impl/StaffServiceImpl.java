package com.qing.tea.service.impl;

import com.qing.tea.entity.Staff;
import com.qing.tea.service.StaffService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class StaffServiceImpl implements StaffService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public long getCount() {
        return mongoTemplate.count(new Query(), Staff.class);
    }

    @Override
    public Staff insert(Staff staff) {
        return mongoTemplate.insert(staff);
    }

    @Override
    public void delete(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Staff.class);
    }

    @Override
    public void update(String id, String name, String value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, Staff.class);
    }

    @Override
    public Staff find(String id) {
        return  mongoTemplate.findById(id, Staff.class);
    }

    @Override
    public List<Staff> findAll() {
        return mongoTemplate.findAll(Staff.class);
    }

    @Override
    public List<Staff> findByCond(String name, String value) {
        Query query=new Query(Criteria.where(name).is(value));
        return mongoTemplate.find(query, Staff.class);
    }

    @Override
    public List<Staff> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where("_id").regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Staff.class);
    }

    @Override
    public List<Staff> findList(int page, int rows) {
        Query query = new Query();
        query.skip(page*rows).limit(rows);
        return mongoTemplate.find(query, Staff.class);
    }
}
