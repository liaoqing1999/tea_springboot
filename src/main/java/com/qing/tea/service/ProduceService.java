package com.qing.tea.service;

import com.qing.tea.entity.Produce;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;

public interface ProduceService {
    public long getCount(Criteria criteria) ;

    public Produce insert(Produce produce);

    public void delete(String id);

    public void update(String id,String name,Object value);

    public void update(Produce produce);

    public Produce find(String id);

    public List<Produce> findAll();

    public List<Produce> findByCond(Criteria criteria);

    public List<Produce> findLike(String name,String searchKey );

    public List<Map> findList(int page, int rows, Criteria criteria);
}
