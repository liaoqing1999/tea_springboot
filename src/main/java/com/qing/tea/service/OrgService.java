package com.qing.tea.service;
import com.qing.tea.entity.Org;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;

public interface OrgService {
    public long getCount(Criteria criteria) ;

    public Org insert(Org org);

    public void delete(String id);

    public void update(String id,String name,Object value);

    public void update(Org org);

    public Org find(String id);

    public List<Org> findAll();

    public List<Org> findByCond(Criteria criteria);

    public List<Org> findLike(String name,String searchKey );

    public List<Org> findList(int page, int rows, Criteria criteria);

    public Map<String, Object> chart(Criteria criteria, String str);
}
