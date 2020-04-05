package com.qing.tea.service.impl;

import com.qing.tea.entity.Dictionary;
import com.qing.tea.service.DictionaryService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;
@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(Dictionary dictionary) {
        mongoTemplate.insert(dictionary);
    }

    @Override
    public void delete(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Dictionary.class);
    }

    @Override
    public void update(String id,String name,String value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, Dictionary.class);
    }

    @Override
    public Dictionary find(String id) {
        Query query=new Query(Criteria.where("id").is(id));
        List<Dictionary> teas = mongoTemplate.find(query, Dictionary.class);
        return teas.get(0);
    }

    @Override
    public List<Dictionary> findAll() {
        return mongoTemplate.findAll(Dictionary.class);
    }

    @Override
    public List<Dictionary> findByCond(String name, String value) {
        Query query=new Query(Criteria.where(name).is(value));
        return mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public List<Dictionary> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where("_id").regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public List<Dictionary> findList(int page, int rows) {
        Query query = new Query();
        query.skip(page*rows).limit(rows);
        return mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public List<Dictionary> findByCodeValue(String typeCode, String valueId) {
        Criteria criteria = Criteria.where("type_code").is(typeCode);
        criteria.and("value_id").is(valueId);
        Query query = new Query(criteria);
        return  mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public long getCount() {
        return mongoTemplate.count(new Query(), Dictionary.class);
    }


}
