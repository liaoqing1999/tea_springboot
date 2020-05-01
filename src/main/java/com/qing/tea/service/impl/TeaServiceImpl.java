package com.qing.tea.service.impl;

import com.qing.tea.entity.Tea;
import com.qing.tea.service.TeaService;
import org.springframework.data.domain.Sort;
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
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), Tea.class);
    }

}
