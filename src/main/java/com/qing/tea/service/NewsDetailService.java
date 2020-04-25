package com.qing.tea.service;
import com.qing.tea.entity.NewsDetail;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface NewsDetailService {
    public long getCount(Criteria criteria) ;

    public NewsDetail insert(NewsDetail newsDetail);

    public void delete(String id);

    public void update(String id, String name, Object value);

    public NewsDetail find(String id);

    public List<NewsDetail> findAll();

    public List<NewsDetail> findByCond(Criteria criteria);

    public List<NewsDetail> findLike(String name, String searchKey);

    public List<NewsDetail> findList(int page, int rows, Criteria criteria);


}
