package com.qing.tea.service.impl;

import com.qing.tea.entity.Dictionary;
import com.qing.tea.service.DictionaryService;
import com.qing.tea.utils.UpdateUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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
    public void delete(Criteria criteria) {
        mongoTemplate.remove(new Query(criteria),Dictionary.class);
    }

    @Override
    public void update(String id,String name,Object value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, Dictionary.class);
    }

    @Override
    public void update(Dictionary dictionary) {
        Query query=new Query(Criteria.where("_id").is(dictionary.getId()));
        Update update = UpdateUtils.getUpdate(dictionary);
        mongoTemplate.updateFirst(query, update, Dictionary.class);
    }

    @Override
    public void updateAll(Criteria criteria,String name,Object value) {
        Query query=new Query(criteria);
        Update update = Update.update(name, value);
        mongoTemplate.updateMulti(query, update, Dictionary.class);
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
    public List<Map> findTypeList(Criteria criteria) {
        criteria.and("type_code").ne("").ne(null);
        criteria.and("value_id").ne("").ne(null);
        AggregationOperation match = Aggregation.match(criteria);
        GroupOperation group = Aggregation.group("type_code").count().as("count").first("type_name").as("typeName");
        Aggregation aggregation = Aggregation.newAggregation(match,group);
        List<Map> results = mongoTemplate.aggregate(aggregation,"dictionary", Map.class).getMappedResults();
        return results;
    }

    @Override
    public List<Dictionary> findByCodeValue(String typeCode, String valueId,String state) {
        Criteria criteria = Criteria.where("type_code").is(typeCode);
        if(valueId!=null&&!valueId.equals("")) criteria.and("value_id").is(valueId);
        if(state!=null&&!state.equals("")) {
            criteria.and("state").is(state);
        }
        Query query = new Query(criteria);
        return  mongoTemplate.find(query, Dictionary.class);
    }

    @Override
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), Dictionary.class);
    }


}
