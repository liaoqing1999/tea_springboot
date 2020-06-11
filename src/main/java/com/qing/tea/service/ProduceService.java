package com.qing.tea.service;

import com.qing.tea.entity.Produce;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;

public interface ProduceService {
    /**
     * 获取数量
     * @param criteria 条件
     * @return
     */
    public long getCount(Criteria criteria) ;

    /**
     * 插入
     * @param produce 实体
     * @return
     */
    public Produce insert(Produce produce);

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
     * @param produce 实体
     */
    public void update(Produce produce);

    /**
     * 通过主键查找
     * @param id 主键id
     * @return
     */
    public Produce find(String id);

    /**
     * 查找全部
     * @return
     */
    public List<Produce> findAll();

    /**
     * 通过条件查找
     * @param criteria 条件
     * @return
     */
    public List<Produce> findByCond(Criteria criteria);

    /**
     * 查找字段包含值
     * @param name 字段名
     * @param searchKey 值
     * @return
     */
    public List<Produce> findLike(String name,String searchKey );

    /**
     * 分页查询
     * @param page 页数
     * @param rows 行数
     * @param criteria 条件
     * @return
     */
    public List<Map> findList(int page, int rows, Criteria criteria);

    /**
     * 图表分析
     * @param criteria 条件
     * @param str 类别分割字段
     * @return
     */
    public Map<String, Object> chart(Criteria criteria, String str);
}
