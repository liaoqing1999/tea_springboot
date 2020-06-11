package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.Org;
import com.qing.tea.entity.Param;
import com.qing.tea.entity.Role;
import com.qing.tea.entity.Staff;
import com.qing.tea.service.OrgService;
import com.qing.tea.service.RoleService;
import com.qing.tea.service.StaffService;
import com.qing.tea.utils.R;
import io.swagger.annotations.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
@Api(tags  = "机构操作接口")
@RestController
@RequestMapping("org")
public class OrgController {
    /**
     * 服务对象
     */
    @Resource
    private OrgService orgService;
    @Resource
    private StaffService staffService;
    @Resource
    private RoleService roleService;

    /**
     * 机构入住
     * @param param 机构和用户的合并参数
     * @return
     */
    @ApiOperation(value = "机构入住", notes="传入机构实体和机构管理员来完成机构入住")
            @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= String.class)
    })
    @PostMapping("join")
    @ResponseBody
    public R join(@ApiParam(value="机构和用户的合并实体参数",required = true)@RequestBody Param param) {
        Criteria criteria = Criteria.where("name").is("orgAdmin");
        List<Role> role = roleService.findByCond(criteria);
        if(role.size()>0){
            param.staff.setRole(role.get(0).getId());
        }
        param.org.setState("1");
        Org org = orgService.insert(param.org);
        param.staff.setOrg(org.getId());
        param.staff.setWork("orgAdmin");
        Staff staff = staffService.insert(param.staff);
        //param.org.setAdmin(staff.getId());
        staffService.update(staff.getId(), "org", org.getId());
        return R.success("success");
    }

    /**
     * 新增机构
     * @param org 机构实体
     * @return
     */
    @ApiOperation(value = "新增机构", notes="新增一条机构记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Org.class)
    })
    @PostMapping("add")
    @ResponseBody
    public R add(@ApiParam(value="机构实体",required = true)@RequestBody Org org) {
        return R.success(orgService.insert(org));
    }

    /**
     * 更新机构
     * @param org 机构实体
     * @return
     */
    @ApiOperation(value = "新增机构", notes="新增一条机构记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Org.class)
    })
    @PostMapping("update")
    @ResponseBody
    public R update(@ApiParam(value="机构实体",required = true)@RequestBody Org org) {
        orgService.update(org);
        return R.success(orgService.find(org.getId()));
    }

    /**
     * 获取机构
     * @param id 机构id
     * @return
     */
    @ApiOperation(value = "获取机构", notes="通过id获取机构")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Org.class)
    })
    @GetMapping("/get")
    @ResponseBody
    public R getOrg(@ApiParam(value="机构id",required = true)@RequestParam(name = "id") String id) {
        return R.success(orgService.find(id));
    }

    /**
     * 删除机构
     * @param id 机构id
     */
    @ApiOperation(value = "删除机构", notes="通过id删除机构")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @DeleteMapping("delete")
    @ResponseBody
    public void delete(@ApiParam(value="机构id",required = true)@RequestParam(name = "id")String  id){
        orgService.delete(id);
    }

    /**
     * 审核机构
     * @param id 机构id
     * @param state 状态
     * @return
     */
    @ApiOperation(value = "审核机构", notes="审核发起入住申请的机构")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Org.class)
    })
    @GetMapping("/auth")
    @ResponseBody
    public R getOrgAuth(
            @ApiParam(value="机构id",required = true)@RequestParam(name = "id") String id,
            @ApiParam(value="状态")@RequestParam(name = "state",defaultValue = "2") String state) {
        orgService.update(id,"state",state);
        return R.success(orgService.find(id));
    }

    /**
     * 获取状态下所有机构
     * @param state 状态
     * @return
     */
    @ApiOperation(value = "获取状态下所有机构", notes="获取某个状态下所有机构")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Org[].class)
    })
    @GetMapping("/getAll")
    @ResponseBody
    public R getOrgAll( @ApiParam(value="状态")@RequestParam(required = false,name = "state") String state) {
        Criteria criteria = new Criteria();
        if(state!=null&&!state.equals("")){
             criteria = Criteria.where("state").is(state);
        }
        return R.success(orgService.findByCond(criteria));
    }


    /**
     * 分页获取机构
     * @param page 页数
     * @param rows 行数
     * @param place 地点
     * @param state 状态
     * @return
     */
    @ApiOperation(value = "分页获取机构", notes="分页获取机构")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Map.class)
    })
    @GetMapping("getPage")
    @ResponseBody
    public R getOrgPage( @ApiParam(value="页数")@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                         @ApiParam(value="行数") @RequestParam(name = "rows", required = false, defaultValue = "3") int rows,
                         @ApiParam(value="地点")@RequestParam(required = false, defaultValue = "", name = "place") String place,
                         @ApiParam(value="状态") @RequestParam(required = false, defaultValue = "2", name = "state") String state) {
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


    /**
     * 机构分析
     * @param cond 条件
     * @return
     */
    @ApiOperation(value = "机构分析", notes="机构图表分析")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Map.class)
    })
    @GetMapping("chart")
    @ResponseBody
    public R<Map<String, Object>> chart(@ApiParam(value="条件")@RequestParam(name = "cond",required =false) String cond) {
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
