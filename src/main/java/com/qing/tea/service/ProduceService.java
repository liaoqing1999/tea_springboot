package com.qing.tea.service;

import com.qing.tea.entity.Produce;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface ProduceService {
    public long getCount(Criteria criteria) ;

    public Produce insert(Produce produce);

    public void delete(String id);

    public void update(String id,String name,Object value);

    public Produce find(String id);

    public List<Produce> findAll();

    public List<Produce> findByCond(String name,String value);

    public List<Produce> findLike(String name,String searchKey );

    public List<Produce> findList(int page, int rows, Criteria criteria);
}
