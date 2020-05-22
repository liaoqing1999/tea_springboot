package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.Org;
import com.qing.tea.entity.Param;
import com.qing.tea.entity.Role;
import com.qing.tea.entity.Staff;
import com.qing.tea.service.OrgService;
import com.qing.tea.service.RoleService;
import com.qing.tea.service.StaffService;
import com.qing.tea.utils.R;
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
@RequestMapping("org")
public class OrgController {
    @Resource
    private OrgService orgService;
    @Resource
    private StaffService staffService;
    @Resource
    private RoleService roleService;

    @RequestMapping("join")
    @ResponseBody
    public R join(@RequestBody Param param) {
        Criteria criteria = Criteria.where("name").is("orgAdmin");
        List<Role> role = roleService.findByCond(criteria);
        if(role.size()>0){
            param.staff.setRole(role.get(0).getId());
        }
        Org org = orgService.insert(param.org);
        param.staff.setOrg(org.getId());
        param.staff.setWork("orgAdmin");
        param.org.setState("1");
        Staff staff = staffService.insert(param.staff);
        //param.org.setAdmin(staff.getId());
        staffService.update(staff.getId(), "org", org.getId());
        return R.success("success");
    }
    @RequestMapping("add")
    @ResponseBody
    public R add(@RequestBody Org org) {
        return R.success(orgService.insert(org));
    }

    @RequestMapping("update")
    @ResponseBody
    public R update(@RequestBody Org org) {
        orgService.update(org);
        return R.success(orgService.find(org.getId()));
    }
    @RequestMapping("/get")
    @ResponseBody
    public R getOrg(@RequestParam(name = "id") String id) {
        return R.success(orgService.find(id));
    }

    @RequestMapping("delete")
    @ResponseBody
    public void delete(@RequestParam(name = "id")String  id){
        orgService.delete(id);
    }

    @RequestMapping("/auth")
    @ResponseBody
    public R getOrgAuth(@RequestParam(name = "id") String id,@RequestParam(name = "state",defaultValue = "2") String state) {
        orgService.update(id,"state",state);
        return R.success(orgService.find(id));
    }
    @RequestMapping("/getAll")
    @ResponseBody
    public R getOrgAll(@RequestParam(required = false,name = "state") String state) {
        Criteria criteria = new Criteria();
        if(state!=null&&!state.equals("")){
             criteria = Criteria.where("state").is(state);
        }
        return R.success(orgService.findByCond(criteria));
    }
    @RequestMapping("getPage")
    @ResponseBody
    public R getOrgPage(@RequestParam(name = "page") int page,
                        @RequestParam(name = "rows") int rows,
                        @RequestParam(required = false, defaultValue = "", name = "place") String place,
                        @RequestParam(required = false, defaultValue = "2", name = "state") String state) {
        Pattern pattern = Pattern.compile("^.*" + place + ".*$");
        Criteria criteria = Criteria.where("place").regex(pattern);
        if(state.equals("all")){
            List<String> states = new ArrayList<String>();
            states.add("0");
            states.add("2");
            states.add("3");
            criteria.and("state").in(states);
        }else{
            criteria.and("state").is(state);
        }
        List<Org> list = orgService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, orgService.getCount(criteria), page, rows);
        return R.success(result);
    }

    @RequestMapping("chart")
    @ResponseBody
    public R<Map<String, Object>> chart(@RequestParam(name = "cond",required =false) String cond) {
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        String str = "slice(placeChart,1)";
        if(parse!=null) {
            if (parse.get("type") != null) {
                if(parse.get("type").equals("province")){
                    str =   "slice(placeChart,1)";
                }else if(parse.get("type").equals("city")){
                    str =   "slice(placeChart,2)";
                }else if(parse.get("type").equals("area")){
                    str =  "slice(placeChart,3)";
                }
            }
            if(parse.get("place")!=null){
                Pattern pattern = Pattern.compile("^.*" + parse.get("place") + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
                criteria.and("place").regex(pattern);
            }
        }
        return R.success(orgService.chart(criteria,str));
    }
}
