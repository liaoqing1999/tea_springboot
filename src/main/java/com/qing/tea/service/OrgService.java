package com.qing.tea.service;
import com.qing.tea.entity.Org;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface OrgService {
    public long getCount(Criteria criteria) ;

    public Org insert(Org org);

    public void delete(String id);

    public void update(String id,String name,Object value);

    public void update(String id,Org org);

    public Org find(String id);

    public List<Org> findAll();

    public List<Org> findByCond(Criteria criteria);

    public List<Org> findLike(String name,String searchKey );

    public List<Org> findList(int page, int rows, Criteria criteria);


}
