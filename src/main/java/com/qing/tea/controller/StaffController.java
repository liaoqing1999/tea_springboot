package com.qing.tea.controller;

import com.qing.tea.entity.Staff;
import com.qing.tea.service.StaffService;
import com.qing.tea.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

}
