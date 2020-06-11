package com.qing.tea.service;

import com.qing.tea.entity.Dictionary;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;

public interface DictionaryService {
    /**
     * 获取数量
     * @param criteria 条件
     * @return
     */
    public long getCount(Criteria criteria) ;

    /**
     * 插入
     * @param dictionary 实体
     * @return
     */
    public Dictionary insert(Dictionary dictionary);

    /**
     * 删除
     * @param id 主键id
     */
    public void delete(String id);

    /**
     * 根据条件删除
     * @param criteria 条件
     */
    public void delete(Criteria criteria);

    /**
     * 更新字段
     * @param id 主键id
     * @param name 字段名
     * @param value 更新值
     */
    public void update(String id,String name,Object value);

    /**
     * 更新实体
     * @param dictionary 实体
     */
    public void update(Dictionary dictionary);

    /**
     * 根据条件更新全部
     * @param criteria 条件
     * @param name 字段名
     * @param value 更新值
     */
    public void updateAll(Criteria criteria,String name,Object value);

    /**
     * 通过主键查找
     * @param id 主键id
     * @return
     */
    public Dictionary find(String id);

    /**
     * 查找全部
     * @return
     */
    public List<Dictionary> findAll();

    /**
     * 通过条件查找
     * @param criteria 条件
     * @return
     */
    public List<Dictionary> findByCond(Criteria criteria);


    /**
     * 查找字段包含值
     * @param name 字段名
     * @param searchKey 值
     * @return
     */
    public List<Dictionary> findLike(String name,String searchKey );


    /**
     * 查找字典类型
     * @param criteria
     * @return
     */
    public List<Map> findTypeList(Criteria criteria);

    /**
     * 通过类别和值查找字典
     * @param typeCode 类别code
     * @param valueId 值id
     * @param state 状态
     * @return
     */
    public List<Dictionary> findByCodeValue(String typeCode,String valueId,String state);
}
