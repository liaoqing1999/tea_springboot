package com.qing.tea.service.impl;
import com.qing.tea.entity.News;
import com.qing.tea.entity.NewsDetail;
import com.qing.tea.entity.Staff;
import com.qing.tea.service.NewsDetailService;
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
public class NewsDetailServiceImpl implements NewsDetailService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), News.class);
    }

    @Override
    public NewsDetail insert(NewsDetail newsDetail) {
        mongoTemplate.insert(newsDetail);
        return newsDetail;
    }

    @Override
    public void delete(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,NewsDetail.class);
    }

    @Override
    public void update(String id, String name, Object value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, NewsDetail.class);
    }

    @Override
    public NewsDetail find(String id) {
        return mongoTemplate.findById(id,NewsDetail.class);
    }

    @Override
    public List<NewsDetail> findAll() {
        return mongoTemplate.findAll(NewsDetail.class);
    }

    @Override
    public List<NewsDetail> findByCond(Criteria criteria) {
        Query query=new Query(criteria);
        return mongoTemplate.find(query, NewsDetail.class);
    }
    @Override
    public List<Map> findByNews(Criteria criteria) {
        LookupOperation staff= LookupOperation.newLookup().
                from("staff").  //关联从表名
                localField("user").
                foreignField("_id").
                as("staff");
        AggregationOperation match = Aggregation.match(criteria);
        Aggregation aggregation = Aggregation.newAggregation(staff,match);
        List<Map> results = mongoTemplate.aggregate(aggregation,"news_detail", Map.class).getMappedResults();
        for(Map map:results){
            if(map.get("user")!=null){
                map.put("user",map.get("user").toString()) ;
            }
            if(map.get("staff")!=null){
                ArrayList<Map> userList = (ArrayList<Map>) map.get("staff");
                if(userList.size()>0){
                    map.put("userName",userList.get(0).get("name"));
                }
            }
            if(map.get("news")!=null){
                map.put("news",map.get("news").toString()) ;
            }
            if(map.get("_id")!=null){
                map.put("id",map.get("_id").toString()) ;
            }
            map.remove("staff");
        }
        return results;
    }
    @Override
    public List<NewsDetail> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where(name).regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, NewsDetail.class);
    }

    @Override
    public List<NewsDetail> findList(int page, int rows, Criteria criteria) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        Query query = new Query(criteria);
        query.skip((page-1)*rows).limit(rows);
        return mongoTemplate.find(query, NewsDetail.class);
    }
}
