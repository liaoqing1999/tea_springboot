package com.qing.tea.service;

import com.qing.tea.entity.Dictionary;

import java.util.List;

public interface DictionaryService {
    public long getCount() ;

    public Dictionary insert(Dictionary dictionary);

    public void delete(String id);

    public void update(String id,String name,Object value);

    public Dictionary find(String id);

    public List<Dictionary> findAll();

    public List<Dictionary> findByCond(String name,String value);

    public List<Dictionary> findLike(String name,String searchKey );

    public List<Dictionary> findList(int page,int rows);

    public List<Dictionary> findByCodeValue(String typeCode,String valueId);
}
