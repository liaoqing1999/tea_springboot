package com.qing.tea.service.impl;

import com.qing.tea.entity.Role;
import com.qing.tea.service.RoleService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public long getCount() {
        return mongoTemplate.count(new Query(), Role.class);
    }

    @Override
    public Role insert(Role role) {
        mongoTemplate.insert(role);
        return role;
    }

    @Override
    public void delete(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Role.class);
    }

    @Override
    public void update(String id, String name, String value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, Role.class);
    }

    @Override
    public Role find(String id) {
        return mongoTemplate.findById(id,Role.class);
    }

    @Override
    public List<Role> findAll() {
        return mongoTemplate.findAll(Role.class);
    }

    @Override
    public List<Role> findByCond(String name, String value) {
        Query query=new Query(Criteria.where(name).is(value));
        return mongoTemplate.find(query, Role.class);
    }

    @Override
    public List<Role> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where("_id").regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Role.class);
    }

    @Override
    public List<Role> findList(int page, int rows) {
        Query query = new Query();
        query.skip(page*rows).limit(rows);
        return mongoTemplate.find(query, Role.class);
    }
}
