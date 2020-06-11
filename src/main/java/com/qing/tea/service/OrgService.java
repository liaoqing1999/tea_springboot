package com.qing.tea.service;
import com.qing.tea.entity.Org;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;

public interface OrgService {
    /**
     * 获取数量
     * @param criteria 条件
     * @return
     */
    public long getCount(Criteria criteria) ;

    /**
     * 插入
     * @param org 实体
     * @return
     */
    public Org insert(Org org);

    /**
     * 删除
     * @param id 主键id
     */
    public void delete(String id);

    /**
     * 更新字段
     * @param id 主键id
     * @param name 字段名
     * @param value 更新值
     */
    public void update(String id,String name,Object value);

    /**
     * 更新实体
     * @param org 实体
     */
    public void update(Org org);

    /**
     * 通过主键查找
     * @param id 主键id
     * @return
     */
    public Org find(String id);

    /**
     * 查找全部
     * @return
     */
    public List<Org> findAll();

    /**
     * 通过条件查找
     * @param criteria 条件
     * @return
     */
    public List<Org> findByCond(Criteria criteria);

    /**
     * 查找字段包含值
     * @param name 字段名
     * @param searchKey 值
     * @return
     */
    public List<Org> findLike(String name,String searchKey );

    /**
     * 分页查询
     * @param page 页数
     * @param rows 行数
     * @param criteria 条件
     * @return
     */
    public List<Org> findList(int page, int rows, Criteria criteria);

    /**
     * 图表分析
     * @param criteria 条件
     * @param str 地点分割字段
     * @return
     */
    public Map<String, Object> chart(Criteria criteria, String str);
}
