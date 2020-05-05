package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.Staff;
import com.qing.tea.service.StaffService;
import com.qing.tea.utils.R;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("staff")
public class StaffController {
    @Resource
    StaffService staffService;

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
        staffService.update(staff.getId(),"name",staff.getName());
        staffService.update(staff.getId(),"email",staff.getEmail());
        staffService.update(staff.getId(),"real_name",staff.getRealName());
        staffService.update(staff.getId(),"phone",staff.getPhone());
        staffService.update(staff.getId(),"card",staff.getCard());
        staffService.update(staff.getId(),"work",staff.getWork());
        staffService.update(staff.getId(),"img",staff.getImg());
        staffService.update(staff.getId(),"role",new ObjectId(staff.getRole()) );
        staffService.update(staff.getId(),"org",new ObjectId(staff.getOrg()));
        staffService.update(staff.getId(),"state",staff.getState());
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
}
