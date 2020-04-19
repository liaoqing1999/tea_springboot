package com.qing.tea.controller;

import com.qing.tea.entity.Org;
import com.qing.tea.entity.Param;
import com.qing.tea.entity.Staff;
import com.qing.tea.service.OrgService;
import com.qing.tea.service.StaffService;
import com.qing.tea.utils.R;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    @RequestMapping("join")
    @ResponseBody
    public R join(@RequestBody Param param){
        param.staff.setWork("admin");
        param.org.setState("1");
        Staff staff = staffService.insert(param.staff);
        param.org.setAdmin(staff.getId());
        Org org = orgService.insert(param.org);
        staffService.update(staff.getId(),"org",org.getId());
        return R.success("success");
    }
    @RequestMapping("/getOrg")
    @ResponseBody
    public R getOrg(@RequestParam(name = "id")String id){
        return R.success(orgService.find(id));
    }

    @RequestMapping("getPage")
    @ResponseBody
    public R getRolePage(@RequestParam(name = "page")int page, @RequestParam(name = "rows")int rows,@RequestParam(required =false,defaultValue="",name = "place")String place){
        Pattern pattern = Pattern.compile("^.*" + place + ".*$");
        Criteria criteria = Criteria.where("place").regex(pattern);
        criteria.andOperator( Criteria.where("state").is("2"));
        List<Org> list = orgService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, orgService.getCount(criteria), page, rows);
        return R.success(result);
    }
}
