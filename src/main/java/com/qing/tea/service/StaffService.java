package com.qing.tea.service;
import com.qing.tea.entity.Staff;
import com.qing.tea.utils.R;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

public interface StaffService {
    /**
     * 根据条件获取数量
     * @param criteria
     * @return
     */
    public long getCount(Criteria criteria) ;

    /**
     * 新增用户
     * @param staff
     * @return
     */
    public Staff insert(Staff staff);

    /**
     * 新增用户集合
     * @param staff
     */
    public void insert(List<Staff> staff);

    /**
     * 根据id删除用户
     * @param id
     */
    public void delete(String id);

    /**
     * 根据条件删除用户
     * @param criteria
     */
    public void delete(Criteria criteria);

    /**
     * 更新用户某个字段
     * @param id
     * @param name
     * @param value
     */
    public void update(String id,String name,Object value);

    /**
     * 更新用户实体
     * @param staff
     */
    public void update(Staff staff);

    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    public Staff find(String id);

    /**
     * 所有用户
     * @return
     */
    public List<Staff> findAll();

    /**
     * 通过条件查找用户
     * @param criteria
     * @return
     */
    public List<Staff> findByCond(Criteria criteria);

    /**
     * 通过条件查找用户
     * @param query
     * @return
     */
    public List<Staff> findByCond(Query query);

    /**
     * 搜索某个字段like的用户
     * @param name
     * @param searchKey
     * @return
     */
    public List<Staff> findLike(String name,String searchKey );

    /**
     * 用户分页查询
     * @param page
     * @param rows
     * @param criteria
     * @return
     */
    public List<Map> findList(int page, int rows, Criteria criteria);

    /**
     * 用户图表分析
     * @param criteria
     * @param str
     * @return
     */
    public Map<String, Object> chart(Criteria criteria, String str);

    /**
     * 发送邮箱验证码
     * @param id
     * @param operation
     */
    public R sendVerification(String id, String operation);

    /**
     * 通过邮箱验证码更新密码
     * @param id
     * @param newPassword
     * @param code
     * @param operation
     */
    public R updatePWByVerificationCode(String id, String newPassword,String code,String operation);

    /**
     * 验证邮箱验证码
     * @param email
     * @param code
     * @param operation
     */
    public R verificationCheck(String email,String code, String operation);
}
