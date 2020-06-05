package com.qing.tea.service.impl;
import com.qing.tea.entity.Org;
import com.qing.tea.entity.Produce;
import com.qing.tea.service.ProduceService;
import com.qing.tea.utils.UpdateUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ProduceServiceImpl implements ProduceService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), Produce.class);
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
    public void update(Produce produce) {
        Query query=new Query(Criteria.where("_id").is(produce.getId()));
        Update update = UpdateUtils.getUpdate(produce);
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
    public List<Produce> findByCond(Criteria criteria) {
        Query query=new Query(criteria);
        return mongoTemplate.find(query, Produce.class);
    }

    @Override
    public List<Produce> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where(name).regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Produce.class);
    }

    @Override
    public List<Map> findList(int page, int rows, Criteria criteria) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        Query query = new Query(criteria);
        query.skip((page-1)*rows).limit(rows);
        LookupOperation org= LookupOperation.newLookup().
                from("org").  //关联从表名
                localField("org").
                foreignField("_id").
                as("ProduceOrg");
        AggregationOperation match = Aggregation.match(criteria);
        AggregationOperation skip = Aggregation.skip((long) (page-1)*rows);
        AggregationOperation limit = Aggregation.limit((long) rows);
        Aggregation aggregation = Aggregation.newAggregation(org,match,skip,limit);
        List<Map> results = mongoTemplate.aggregate(aggregation,"produce", Map.class).getMappedResults();
        for(Map map:results){
            if(map.get("org")!=null){
                map.put("org",map.get("org").toString()) ;
            }
            if(map.get("_id")!=null){
                map.put("id",map.get("_id").toString()) ;
            }
            if(map.get("ProduceOrg")!=null){
                try{
                    ArrayList<Org> orgList = (ArrayList<Org>) map.get("ProduceOrg");
                    if(orgList.size()>0){
                        map.put("orgName",orgList.get(0).getName());
                    }
                }catch(ClassCastException e){
                    ArrayList<LinkedHashMap> orgList = (ArrayList<LinkedHashMap>) map.get("ProduceOrg");
                    if(orgList.size()>0){
                        map.put("orgName",orgList.get(0).get("name"));
                    }
                }
            }
        }
        return results;
    }


    @Override
    public Map<String, Object> chart(Criteria criteria, String str) {
        List<AggregationOperation> typeOper = new ArrayList<AggregationOperation>();
        AggregationOperation match = Aggregation.match(criteria);
        typeOper.add(match);
        ProjectionOperation day = Aggregation.project().andExpression(str).as("type");
        typeOper.add(day);
        GroupOperation dateGroup = Aggregation.group("type").count().as("count");
        typeOper.add(dateGroup);
        Aggregation typeAggregation = Aggregation.newAggregation(typeOper);
        List<Map> type = mongoTemplate.aggregate(typeAggregation,"produce", Map.class).getMappedResults();
        long total = mongoTemplate.count(new Query(), Produce.class);
        GroupOperation gradeGroup = Aggregation.group("grade").count().as("count");
        Aggregation gradeAggregation = Aggregation.newAggregation(gradeGroup);
        List<Map> grade = mongoTemplate.aggregate(gradeAggregation,"produce", Map.class).getMappedResults();

        LookupOperation orgOper= LookupOperation.newLookup().
                from("org").  //关联从表名
                localField("org").
                foreignField("_id").
                as("ProduceOrg");
        GroupOperation orgGroup = Aggregation.group("org").count().as("count").first("ProduceOrg.name").as("orgName");
        Aggregation aggregation = Aggregation.newAggregation(orgOper,orgGroup);
        List<Map> org = mongoTemplate.aggregate(aggregation,"produce", Map.class).getMappedResults();

        Map<String,Object> results = new HashMap<String, Object>();
        results.put("grade",grade);
        results.put("org",org);
        results.put("total",total);
        results.put("type",type);
        return results;
    }
}
