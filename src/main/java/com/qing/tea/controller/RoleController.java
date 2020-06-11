package com.qing.tea.controller;

import com.qing.tea.entity.Role;
import com.qing.tea.service.RoleService;
import com.qing.tea.utils.R;
import io.swagger.annotations.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Api(tags  = "角色操作接口")
@RestController
@RequestMapping("role")
public class RoleController {
    /**
     * 服务对象
     */
    @Resource
    RoleService roleService;


    /**
     * 分页获取角色
     * @param page 页数
     * @param rows 行数
     * @return
     */
    @ApiOperation(value = "分页获取角色", notes="分页获取角色")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Map.class)
    })
    @GetMapping("getPage")
    @ResponseBody
    public R getRolePage(
            @ApiParam(value="页数") @RequestParam(name = "page",required =false,defaultValue = "1")int page,
            @ApiParam(value="行数") @RequestParam(name = "rows",required =false,defaultValue = "10")int rows){
        List<Role> list = roleService.findList(page, rows, new Criteria());
        Map<String, Object> result = MapReuslt.mapPage(list, roleService.getCount(new Criteria()), page, rows);
        return R.success(result);
    }


    /**
     * 删除角色
     * @param id 角色id
     */
    @ApiOperation(value = "删除角色", notes="根据id删除角色")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @DeleteMapping("delete")
    @ResponseBody
    public void delete(  @ApiParam(value="角色id",required = true) @RequestParam(name = "id")String  id){
        roleService.delete(id);
    }

    /**
     * 新增角色
     * @param role 角色实体
     * @return
     */
    @ApiOperation(value = "新增角色", notes="新增一条角色记录",response= Role.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @PostMapping("add")
    @ResponseBody
    public R<Role> add(@ApiParam(value="角色实体",required = true)@RequestBody Role  role){
        return R.success(roleService.insert(role));
    }


    /**
     * 通过角色名查找角色
     * @param name 角色名
     * @return
     */
    @ApiOperation(value = "通过角色名查找角色", notes="通过角色名查找角色",response= Role.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @GetMapping("findByName")
    @ResponseBody
    public R<List<Role>> findByName(@ApiParam(value="角色名",required = true)@RequestParam(name = "name")String  name){
        Criteria criteria = Criteria.where("name").is(name);
        return R.success(roleService.findByCond(criteria));
    }

    /**
     * 获取除超级管理员外的所有角色
     * @return
     */
    @ApiOperation(value = "获取除超级管理员外的所有角色", notes="获取除超级管理员外的所有角色",response= Role[].class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @GetMapping("getAll")
    @ResponseBody
    public R<List<Role>> getRoleAll(){
        return R.success(roleService.findAllDefault());
    }


    /**
     * 获取角色
     * @param id 角色id
     * @return
     */
    @ApiOperation(value = "获取角色", notes="通过id获取角色",response= Role.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @GetMapping("get")
    @ResponseBody
    public R<Role> find(@ApiParam(value="角色名",required = true)@RequestParam(name = "id")String  id){
        return R.success(roleService.find(id));
    }

    /**
     * 更新角色权限
     * @param role 角色实体
     * @return
     */
    @ApiOperation(value = "更新角色权限", notes="更新角色权限",response= Role.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @PostMapping("update")
    @ResponseBody
    public R<Role> updateRole(@ApiParam(value="角色实体",required = true)@RequestBody Role  role){
        roleService.update(role.getId(),"menu",role.getMenu());
        roleService.update(role.getId(),"auth_name",role.getAuthName());
        roleService.update(role.getId(),"auth_time",role.getAuthTime());
        return R.success(roleService.find(role.getId()));
    }
}
