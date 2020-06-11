package com.qing.tea.service.impl;

import com.qing.tea.entity.Role;
import com.qing.tea.service.RoleService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), Role.class);
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
    public void update(String id, String name, Object value) {
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
    public List<Role> findAllDefault() {
        Criteria criteria =new Criteria();
        List<Role> roles = mongoTemplate.find(new Query(criteria),Role.class);
        List<Role> result = new ArrayList<Role>();
        for(Role role:roles){
            if(!role.getName().equals("superAdmin")){
                result.add(role);
            }
        }
        return result;
    }

    @Override
    public List<Role> findByCond(Criteria criteria) {
        Query query=new Query(criteria);
        return mongoTemplate.find(query, Role.class);
    }

    @Override
    public List<Role> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where(name).regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Role.class);
    }

    @Override
    public List<Role> findList(int page, int rows,Criteria criteria) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        Query query = new Query();
        query.skip((page-1)*rows).limit(rows);
        return mongoTemplate.find(query, Role.class);
    }
}
