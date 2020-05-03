package com.qing.tea.service.impl;

import com.qing.tea.entity.Staff;
import com.qing.tea.service.StaffService;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class StaffServiceImpl implements StaffService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), Staff.class);
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
    public void update(String id, String name, Object value) {
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
    public List<Staff> findByCond(Criteria criteria) {
        Query query=new Query(criteria);
        return mongoTemplate.find(query, Staff.class);
    }

    @Override
    public List<Staff> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where(name).regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Staff.class);
    }

    @Override
    public List<Map> findList(int page, int rows,Criteria criteria) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        LookupOperation role= LookupOperation.newLookup().
                from("role").  //关联从表名
                localField("role").
                foreignField("_id").
                as("staffRole");
        LookupOperation org= LookupOperation.newLookup().
                from("org").  //关联从表名
                localField("org").
                foreignField("_id").
                as("staffOrg");
        AggregationOperation match = Aggregation.match(criteria);
        AggregationOperation skip = Aggregation.skip((long) (page-1)*rows);
        AggregationOperation limit = Aggregation.limit((long) rows);
        Aggregation aggregation = Aggregation.newAggregation(role,org,match,skip,limit);
        List<Map> results = mongoTemplate.aggregate(aggregation,"staff", Map.class).getMappedResults();
        for(Map map:results){
            if(map.get("org")!=null){
                map.put("org",map.get("org").toString()) ;
            }
            map.put("id",map.get("_id").toString()) ;
            map.put("role",map.get("role").toString()) ;
        }
        return results;
    }
}
