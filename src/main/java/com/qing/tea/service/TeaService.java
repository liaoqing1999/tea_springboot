package com.qing.tea.service;

import com.qing.tea.entity.Tea;

import java.util.List;

public interface TeaService {
    public long getCount() ;

    public Tea insert(Tea tea);

    public void delete(String id);

    public void update(String id,String name,String value);

    public Tea find(String id);

    public List<Tea> findAll();

    public List<Tea> findByCond(String name,String value);

    public List<Tea> findLike(String name,String searchKey );

    public List<Tea> findList(int page,int rows);
}
