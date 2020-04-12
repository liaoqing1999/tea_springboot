package com.qing.tea.service;
import com.qing.tea.entity.Role;

import java.util.List;

public interface RoleService {
    public long getCount() ;

    public Role insert(Role role);

    public void delete(String id);

    public void update(String id,String name,String value);

    public Role find(String id);

    public List<Role> findAll();

    public List<Role> findByCond(String name,String value);

    public List<Role> findLike(String name,String searchKey );

    public List<Role> findList(int page,int rows);


}
