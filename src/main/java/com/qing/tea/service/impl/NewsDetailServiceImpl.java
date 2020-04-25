package com.qing.tea.service.impl;
import com.qing.tea.entity.News;
import com.qing.tea.entity.NewsDetail;
import com.qing.tea.service.NewsDetailService;
import com.qing.tea.service.NewsService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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
