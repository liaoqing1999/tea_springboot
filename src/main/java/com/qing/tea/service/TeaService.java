package com.qing.tea.service;

import com.qing.tea.entity.Tea;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface TeaService {
    public long getCount(Criteria criteria) ;

    public Tea insert(Tea tea);

    public void delete(String id);

    public void update(String id,String name,Object value);

    public void update(Tea tea);

    public Tea find(String id);

    public List<Tea> findAll();

    public List<Tea> findByCond(Criteria criteria);

    public List<Tea> findLike(String name,String searchKey );

    public List<Tea> findList(int page, int rows, Criteria criteria);
}
