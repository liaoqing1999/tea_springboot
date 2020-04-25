package com.qing.tea.controller;

import com.qing.tea.entity.Role;
import com.qing.tea.service.RoleService;
import com.qing.tea.utils.R;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("role")
public class RoleController {
    @Resource
    RoleService roleService;

    @RequestMapping("getPage")
    @ResponseBody
    public R getRolePage(@RequestParam(name = "page")int page, @RequestParam(name = "rows")int rows){
        List<Role> list = roleService.findList(page, rows, new Criteria());
        Map<String, Object> result = MapReuslt.mapPage(list, roleService.getCount(new Criteria()), page, rows);
        return R.success(result);
    }

    @RequestMapping("delete")
    @ResponseBody
    public void delete(@RequestParam(name = "id")String  id){
        roleService.delete(id);
    }
    @RequestMapping("add")
    @ResponseBody
    public R<Role> add(@RequestBody Role  role){
        return R.success(roleService.insert(role));
    }
    @RequestMapping("findByName")
    @ResponseBody
    public R<List<Role>> findByName(@RequestParam(name = "name")String  name){
        Criteria criteria = Criteria.where("name").is(name);
        return R.success(roleService.findByCond(criteria));
    }
    @RequestMapping("find")
    @ResponseBody
    public R<Role> find(@RequestParam(name = "id")String  id){
        return R.success(roleService.find(id));
    }
    @RequestMapping("update")
    @ResponseBody
    public R<Role> updateRole(@RequestBody Role  role){
        roleService.update(role.getId(),"menu",role.getMenu());
        roleService.update(role.getId(),"auth_name",role.getAuthName());
        roleService.update(role.getId(),"auth_time",role.getAuthTime());
        return R.success(roleService.find(role.getId()));
    }
}
