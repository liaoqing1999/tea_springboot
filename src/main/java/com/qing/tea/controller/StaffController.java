package com.qing.tea.controller;

import com.qing.tea.entity.Staff;
import com.qing.tea.service.StaffService;
import com.qing.tea.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("staff")
public class StaffController {
    @Resource
    StaffService staffService;

    @RequestMapping("login")
    @ResponseBody
    public R login(@RequestParam(name = "name")String name, @RequestParam(name = "password")String password){
        List<Staff> staffList = staffService.findByCond("name", name);
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
    @RequestMapping("update")
    @ResponseBody
    public R update(@RequestBody Staff staff) {
        staffService.update(staff.getId(),"name",staff.getName());
        staffService.update(staff.getId(),"email",staff.getEmail());
        staffService.update(staff.getId(),"real_name",staff.getRealName());
        staffService.update(staff.getId(),"password",staff.getPassword());
        staffService.update(staff.getId(),"phone",staff.getPhone());
        staffService.update(staff.getId(),"card",staff.getCard());
        staffService.update(staff.getId(),"work",staff.getWork());
        staffService.update(staff.getId(),"org",staff.getOrg());
        staffService.update(staff.getId(),"img",staff.getImg());
        return R.success(staffService.find(staff.getId()));
    }
}
