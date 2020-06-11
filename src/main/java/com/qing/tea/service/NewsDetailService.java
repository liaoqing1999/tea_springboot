package com.qing.tea.service;
import com.qing.tea.entity.NewsDetail;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;

public interface NewsDetailService {
    /**
     * 获取数量
     * @param criteria 条件
     * @return
     */
    public long getCount(Criteria criteria) ;

    /**
     * 插入
     * @param newsDetail 实体
     * @return
     */
    public NewsDetail insert(NewsDetail newsDetail);

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
    public void update(String id, String name, Object value);

    /**
     * 通过主键查找
     * @param id 主键id
     * @return
     */
    public NewsDetail find(String id);

    /**
     * 查找全部
     * @return
     */
    public List<NewsDetail> findAll();

    /**
     * 通过条件查找
     * @param criteria 条件
     * @return
     */
    public List<NewsDetail> findByCond(Criteria criteria);

    /**
     * 连接用户表和资讯表查询
     * @param criteria 条件
     * @return
     */
    public List<Map> findByNews(Criteria criteria);

    /**
     * 查找字段包含值
     * @param name 字段名
     * @param searchKey 值
     * @return
     */
    public List<NewsDetail> findLike(String name, String searchKey);

    /**
     * 分页查询
     * @param page 页数
     * @param rows 行数
     * @param criteria 条件
     * @return
     */
    public List<NewsDetail> findList(int page, int rows, Criteria criteria);


}
