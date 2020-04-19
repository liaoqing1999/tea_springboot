package com.qing.tea.service;
import com.qing.tea.entity.Staff;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface StaffService {
    public long getCount(Criteria criteria) ;

    public Staff insert(Staff staff);

    public void delete(String id);

    public void update(String id,String name,Object value);

    public Staff find(String id);

    public List<Staff> findAll();

    public List<Staff> findByCond(String name,String value);

    public List<Staff> findLike(String name,String searchKey );

    public List<Staff> findList(int page, int rows, Criteria criteria);


}
