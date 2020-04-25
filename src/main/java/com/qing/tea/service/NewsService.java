package com.qing.tea.service;
import com.qing.tea.entity.News;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface NewsService {
    public long getCount(Criteria criteria) ;

    public News insert(News news);

    public void delete(String id);

    public void update(String id, String name, Object value);

    public News find(String id);

    public List<News> findAll();

    public List<News> findByCond(Criteria criteria);

    public List<News> findLike(String name, String searchKey);

    public List<News> findList(int page, int rows, Criteria criteria);


}
