package com.qing.tea.service.impl;
import com.qing.tea.entity.News;
import com.qing.tea.entity.Staff;
import com.qing.tea.service.NewsService;
import com.qing.tea.utils.UpdateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class NewsServiceImpl implements NewsService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), News.class);
    }

    @Override
    public News insert(News news) {
        mongoTemplate.insert(news);
        return news;
    }

    @Override
    public void delete(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,News.class);
    }

    @Override
    public void update(String id, String name, Object value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, News.class);
    }

    @Override
    public void update(News news) {
        Query query=new Query(Criteria.where("_id").is(news.getId()));
        Update update = UpdateUtils.getUpdate(news);
        mongoTemplate.updateFirst(query, update, News.class);
    }
    @Override
    public News find(String id) {
        return mongoTemplate.findById(id,News.class);
    }

    @Override
    public List<News> findAll() {
        return mongoTemplate.findAll(News.class);
    }

    @Override
    public List<News> findByCond(Criteria criteria) {
        Query query=new Query(criteria);
        return mongoTemplate.find(query, News.class);
    }

    @Override
    public List<News> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where(name).regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, News.class);
    }

    @Override
    public List<News> findList(int page, int rows, Criteria criteria) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Direction.DESC,"_id"));
        query.skip((page-1)*rows).limit(rows);
        return mongoTemplate.find(query, News.class);
    }

    @Override
    public Map<String, Object> chart(Criteria criteria, String str,String sortNsme){
        List<AggregationOperation> dateOper = new ArrayList<AggregationOperation>();
        AggregationOperation match = Aggregation.match(criteria);
        dateOper.add(match);
        ProjectionOperation day = Aggregation.project().andExpression(str).as("day");
        dateOper.add(day);
        GroupOperation dateGroup = Aggregation.group("day").count().as("count");
        dateOper.add(dateGroup);
        SortOperation sort = Aggregation.sort(Sort.Direction.ASC, "_id");
        dateOper.add(sort);
        Aggregation dateAggregation = Aggregation.newAggregation(dateOper);
        List<Map> date = mongoTemplate.aggregate(dateAggregation,"news", Map.class).getMappedResults();
        long total = mongoTemplate.count(new Query(), News.class);
        GroupOperation typeGroup = Aggregation.group("type").count().as("count");
        Aggregation typeAggregation = Aggregation.newAggregation(typeGroup);
        List<Map> type = mongoTemplate.aggregate(typeAggregation,"news", Map.class).getMappedResults();

        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Direction.DESC,sortNsme));
        query.limit(7);
        List<News> read = mongoTemplate.find(query, News.class);
        Map<String,Object> results = new HashMap<String, Object>();
        results.put("type",type);
        results.put("read",read);
        results.put("total",total);
        results.put("date",date);
        return results;
    }
}
