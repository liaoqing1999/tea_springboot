package com.qing.tea.service.impl;

import com.qing.tea.entity.Org;
import com.qing.tea.entity.Role;
import com.qing.tea.entity.Staff;
import com.qing.tea.service.StaffService;
import com.qing.tea.utils.UpdateUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class StaffServiceImpl implements StaffService {
    @Resource
    private MongoTemplate mongoTemplate;
    @Override
    public long getCount(Criteria criteria) {
        return mongoTemplate.count(new Query(criteria), Staff.class);
    }

    @Override
    public Staff insert(Staff staff) {
        return mongoTemplate.insert(staff);
    }

    @Override
    public void insert(List<Staff> staff) {
       mongoTemplate.insertAll(staff);
    }

    @Override
    public void delete(String id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Staff.class);
    }

    @Override
    public void update(String id, String name, Object value) {
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update(name, value);
        mongoTemplate.updateFirst(query, update, Staff.class);
    }

    @Override
    public void update(Staff staff) {
        Query query=new Query(Criteria.where("_id").is(staff.getId()));
        Update update = UpdateUtils.getUpdate(staff);
        mongoTemplate.updateFirst(query, update, Staff.class);
    }

    @Override
    public Staff find(String id) {
        return  mongoTemplate.findById(id, Staff.class);
    }

    @Override
    public List<Staff> findAll() {
        return mongoTemplate.findAll(Staff.class);
    }

    @Override
    public List<Staff> findByCond(Criteria criteria) {
        Query query=new Query(criteria);
        return mongoTemplate.find(query, Staff.class);
    }

    @Override
    public List<Staff> findLike(String name, String searchKey) {
        Pattern pattern = Pattern.compile("^.*" + searchKey + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
        Criteria criteria = Criteria.where(name).regex(pattern);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Staff.class);
    }

    @Override
    public List<Map> findList(int page, int rows,Criteria criteria) {
        page = page>=0?page:1;
        rows = rows>=0?rows:10;
        LookupOperation role= LookupOperation.newLookup().
                from("role").  //关联从表名
                localField("role").
                foreignField("_id").
                as("staffRole");
        LookupOperation org= LookupOperation.newLookup().
                from("org").  //关联从表名
                localField("org").
                foreignField("_id").
                as("staffOrg");
        AggregationOperation match = Aggregation.match(criteria);
        SortOperation sort = Aggregation.sort(Sort.Direction.ASC, "createTime");
        AggregationOperation skip = Aggregation.skip((long) (page-1)*rows);
        AggregationOperation limit = Aggregation.limit((long) rows);
        Aggregation aggregation = Aggregation.newAggregation(role,org,match,sort,skip,limit);
        List<Map> results = mongoTemplate.aggregate(aggregation,"staff", Map.class).getMappedResults();
        for(Map map:results){
            if(map.get("org")!=null){
                map.put("org",map.get("org").toString()) ;
            }
            if(map.get("_id")!=null){
                map.put("id",map.get("_id").toString()) ;
            }
            if(map.get("role")!=null){
                map.put("role",map.get("role").toString()) ;
            }
            if(map.get("staffOrg")!=null){
                try{
                    ArrayList<LinkedHashMap> orgList = (ArrayList<LinkedHashMap>) map.get("staffOrg");
                    if(orgList.size()>0){
                        map.put("orgName",orgList.get(0).get("name"));
                    }
                }catch(ClassCastException e){
                    ArrayList<Org> orgList = (ArrayList<Org>) map.get("staffOrg");
                    if(orgList.size()>0){
                        map.put("orgName",orgList.get(0).getName());
                    }
                }
            }
            map.remove("staffOrg");
            if(map.get("staffRole")!=null){
                try{
                    ArrayList<LinkedHashMap> roleList = (ArrayList<LinkedHashMap>) map.get("staffRole");
                    if(roleList.size()>0){
                        map.put("roleName",roleList.get(0).get("name"));
                    }
                }catch(ClassCastException e){
                    ArrayList<Role> roleList = (ArrayList<Role>) map.get("staffRole");
                    if(roleList.size()>0){
                        map.put("roleName",roleList.get(0).getName());
                    }
                }
            }
            map.remove("staffRole");
        }
        return results;
    }

    @Override
    public Map<String, Object> chart(Criteria criteria, String str) {
        List<AggregationOperation> dateOper = new ArrayList<AggregationOperation>();
        AggregationOperation match = Aggregation.match(criteria);
        dateOper.add(match);
        ProjectionOperation day = Aggregation.project().andExpression(str).as("day");
        dateOper.add(day);
        GroupOperation dateGroup = Aggregation.group("day").count().as("count");
        dateOper.add(dateGroup);
        SortOperation sort = Aggregation.sort(Sort.Direction.ASC, "_id");
        dateOper.add(sort);
        Aggregation dateAggregation = Aggregation.newAggregation(dateOper);
        List<Map> addTent = mongoTemplate.aggregate(dateAggregation,"staff", Map.class).getMappedResults();
        long total = mongoTemplate.count(new Query(), Staff.class);
        GroupOperation sexGroup = Aggregation.group("sex").count().as("count");
        Aggregation sexAggregation = Aggregation.newAggregation(sexGroup);
        List<Map> sex = mongoTemplate.aggregate(sexAggregation,"staff", Map.class).getMappedResults();
        GroupOperation workGroup = Aggregation.group("work").count().as("count");
        Aggregation workAggregation = Aggregation.newAggregation(workGroup);
        List<Map> work = mongoTemplate.aggregate(workAggregation,"staff", Map.class).getMappedResults();
        Map<String,Object> results = new HashMap<String, Object>();
        results.put("sex",sex);
        results.put("work",work);
        results.put("total",total);
        results.put("addTent",addTent);
        return results;
    }
}
