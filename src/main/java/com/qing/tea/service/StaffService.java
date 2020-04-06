package com.qing.tea.service;
import com.qing.tea.entity.Staff;

import java.util.List;

public interface StaffService {
    public long getCount() ;

    public void insert(Staff staff);

    public void delete(String id);

    public void update(String id,String name,String value);

    public Staff find(String id);

    public List<Staff> findAll();

    public List<Staff> findByCond(String name,String value);

    public List<Staff> findLike(String name,String searchKey );

    public List<Staff> findList(int page,int rows);


}
