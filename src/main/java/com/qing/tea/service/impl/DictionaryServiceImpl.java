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
    public Dictionary insert(Dictionary dictionary) {
        mongoTemplate.insert(dictionary);
        return dictionary;
    }

    @Override
    public void delete(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Dictionary.class);
    }

    @Override
    public void update(String id,String name,Object value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, Dictionary.class);
    }

    @Override
    public Dictionary find(String id) {
        return mongoTemplate.findById(id,Dictionary.class);
    }

    @Override
    public List<Dictionary> findAll() {
        return mongoTemplate.findAll(Dictionary.class);
    }

    @Override
    public List<Dictionary> findByCond(Criteria criteria) {
        Query query=new Query(criteria);
        return mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public List<Dictionary> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where(name).regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public List<Dictionary> findList(int page, int rows,Criteria criteria) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        Query query = new Query(criteria);
        query.skip((page-1)*rows).limit(rows);
        return mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public List<Dictionary> findByCodeValue(String typeCode, String valueId) {
        Criteria criteria = Criteria.where("type_code").is(typeCode);
        if(valueId!=null&&!valueId.equals("")) criteria.and("value_id").is(valueId);
        criteria.and("state").is("2");
        Query query = new Query(criteria);
        return  mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), Dictionary.class);
    }


}
