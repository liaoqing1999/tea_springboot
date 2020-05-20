package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.Org;
import com.qing.tea.entity.Role;
import com.qing.tea.entity.Staff;
import com.qing.tea.service.OrgService;
import com.qing.tea.service.RoleService;
import com.qing.tea.service.StaffService;
import com.qing.tea.utils.R;
import com.qing.tea.utils.RandomDataUtil;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("staff")
public class StaffController {
    @Resource
    StaffService staffService;
    @Resource
    private OrgService orgService;
    @Resource
    RoleService roleService;

    @RequestMapping("login")
    @ResponseBody
    public R login(@RequestParam(name = "name")String name, @RequestParam(name = "password")String password){
        Criteria criteria = Criteria.where("name").is(name);
        List<Staff> staffList = staffService.findByCond(criteria);
        String msg = "";
        if(staffList.size()>0){
            Staff staff = staffList.get(0);
            if(staff.getPassword().equals(password)){
                return R.success(staff);
            }else{
                msg = "密码错误";
            }
        }else{
            msg ="用户名不存在";
        }
        return R.success(msg);
    }
    @RequestMapping("name")
    @ResponseBody
    public R name(@RequestParam(name = "name")String name){
        Criteria criteria = Criteria.where("name").is(name);
        List<Staff> staffList = staffService.findByCond(criteria);
        String msg = "";
        if(staffList.size()>0){
            msg ="用户名已存在";
        }else{
            msg ="";
        }
        return R.success(msg);
    }
    @RequestMapping("update")
    @ResponseBody
    public R update(@RequestBody Staff staff) {
        staffService.update(staff);
        return R.success(staffService.find(staff.getId()));
    }
    @RequestMapping("add")
    @ResponseBody
    public R add(@RequestBody Staff staff) {
        return R.success(staffService.insert(staff));
    }

    @RequestMapping("delete")
    @ResponseBody
    public void delete(@RequestParam(name = "id")String  id){
        staffService.delete(id);
    }
    @RequestMapping("deleteTest")
    @ResponseBody
    public void deleteTest(){
        Criteria criteria = Criteria.where("role").is(new ObjectId("5e987108e0dfa40ea76f664a"));
        staffService.delete(criteria);
    }
    @RequestMapping("password")
    @ResponseBody
    public void delete(@RequestParam(name = "id")String  id,@RequestParam(name = "password")String  password){
        staffService.update(id,"password",password);
    }
    @RequestMapping("getPage")
    @ResponseBody
    public R getStaffPage(@RequestParam(name = "page",required =false,defaultValue = "1")int page, @RequestParam(name = "rows",required =false,defaultValue ="10")int rows,@RequestParam(required =false,name = "cond")String cond){
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        if(parse!=null){
            if(parse.get("name")!=null){
                Pattern pattern = Pattern.compile("^.*" + parse.get("name") + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
                criteria.and("name").regex(pattern);
            }
            if(parse.get("org")!=null){
                criteria.and("org").is(new ObjectId((String) parse.get("org")));
            }
            if(parse.get("role")!=null){
                criteria.and("role").is(new ObjectId((String) parse.get("role")));
            }
            if(parse.get("state")!=null){
                criteria.and("state").is(parse.get("state"));
            }
        }

        List<Map> list = staffService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, staffService.getCount(criteria), page, rows);
        return R.success(result);
    }

    @RequestMapping("chart")
    @ResponseBody
    public R<Map<String, Object>> chart(@RequestParam(name = "cond",required =false) String cond){
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        String str = "substr(add(createTime,28800000),0,10)";
        if(parse!=null) {
            if (parse.get("type") != null) {
                if(parse.get("type").equals("all")){
                   str =  "substr(add(createTime,28800000),0,4)";
                }else if(parse.get("type").equals("year")){
                    str =  "substr(add(createTime,28800000),5,2)";
                }else {
                    str =  "substr(add(createTime,28800000),8,2)";
                }
            }
            if (parse.get("dates") != null) {
                JSONArray date = (JSONArray) parse.get("dates");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startDate=formatter.parse(date.get(0).toString());
                    Date endDate=formatter.parse(date.get(1).toString());
                    criteria.andOperator(
                            Criteria.where("createTime").gte(startDate),
                            Criteria.where("createTime").lt(endDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
        return R.success(staffService.chart(criteria,str));
    }

    @RequestMapping("random")
    @ResponseBody
    public void random(@RequestParam(name = "num")int  num){
        List<Org> orgList = orgService.findAll();
        Criteria criteria =new Criteria();
        List<Role> roles = roleService.findByCond(criteria);
        List<Role> roleList = new ArrayList<Role>();
        for(Role role:roles){
            if(!role.getName().equals("superAdmin")){
                roleList.add(role);
            }
        }
        List<Staff> staffList =new ArrayList<Staff>();
        for(int i=0;i<num;i++){
            Staff staff = new Staff();
            staff.setPassword("123456");
            staff.setWork(RandomDataUtil.getRandomWork());
            staff.setCard(RandomDataUtil.getRandomCard());
            staff.setOrg(RandomDataUtil.getRandomOrg(orgList));
            staff.setRole(RandomDataUtil.getRandomRole(roleList));
            staff.setCreateTime(RandomDataUtil.getRandomDate("2019-08-1","2020-5-22"));
            staff.setEmail(RandomDataUtil.getRandomEmail());
            staff.setPhone(RandomDataUtil.getRandomTel());
            staff.setSex(RandomDataUtil.getRandomSex());
            staff.setRealName(RandomDataUtil.getChineseFamilyName()+RandomDataUtil.getRandomName());
            staff.setState("2");
            String name = "";
            try {
                name = RandomDataUtil.convertChineseToPinyin(staff.getRealName(), false, HanyuPinyinCaseType.LOWERCASE);
            }catch (BadHanyuPinyinOutputFormatCombination e){
                int n = RandomDataUtil.getRandomNum(4,10);
                name = RandomDataUtil.getStringRandom(n);
            }
            staff.setName(name);
            staffList.add(staff);
        }
        staffService.insert(staffList);
    }
}
