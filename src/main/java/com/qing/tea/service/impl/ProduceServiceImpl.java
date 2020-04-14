package com.qing.tea.service.impl;
import com.qing.tea.entity.Produce;
import com.qing.tea.service.ProduceService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ProduceServiceImpl implements ProduceService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public long getCount() {
        return mongoTemplate.count(new Query(), Produce.class);
    }

    @Override
    public Produce insert(Produce produce) {
        mongoTemplate.insert(produce);
        return produce;
    }

    @Override
    public void delete(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Produce.class);
    }

    @Override
    public void update(String id, String name, Object value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, Produce.class);
    }

    @Override
    public Produce find(String id) {
        return mongoTemplate.findById(id,Produce.class);
    }

    @Override
    public List<Produce> findAll() {
        return mongoTemplate.findAll(Produce.class);
    }

    @Override
    public List<Produce> findByCond(String name, String value) {
        Query query=new Query(Criteria.where(name).is(value));
        return mongoTemplate.find(query, Produce.class);
    }

    @Override
    public List<Produce> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where("_id").regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Produce.class);
    }

    @Override
    public List<Produce> findList(int page, int rows) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        Query query = new Query();
        query.skip((page-1)*rows).limit(rows);
        return mongoTemplate.find(query, Produce.class);
    }
}
