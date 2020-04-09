package com.qing.tea.controller;

import com.qing.tea.entity.Org;
import com.qing.tea.entity.Param;
import com.qing.tea.entity.Staff;
import com.qing.tea.service.OrgService;
import com.qing.tea.service.StaffService;
import com.qing.tea.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    @RequestMapping("test")
    @ResponseBody
    public R test(){
        staffService.find("5e80a353790e9318bc605db4");
        return R.success("");
    }
}
