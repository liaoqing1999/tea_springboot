package com.qing.tea.service;

import com.qing.tea.entity.Dictionary;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;

public interface DictionaryService {
    public long getCount(Criteria criteria) ;

    public Dictionary insert(Dictionary dictionary);

    public void delete(String id);

    public void delete(Criteria criteria);

    public void update(String id,String name,Object value);

    public void update(Dictionary dictionary);

    public void updateAll(Criteria criteria,String name,Object value);

    public Dictionary find(String id);

    public List<Dictionary> findAll();

    public List<Dictionary> findByCond(Criteria criteria);

    public List<Dictionary> findLike(String name,String searchKey );

    public List<Map> findTypeList(Criteria criteria);

    public List<Dictionary> findByCodeValue(String typeCode,String valueId,String state);
}
