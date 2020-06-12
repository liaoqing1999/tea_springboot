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
import io.swagger.annotations.*;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
@Api(tags  = "用户操作接口")
@RestController
@RequestMapping("staff")
public class StaffController {
    /**
     * 服务对象
     */
    @Resource
    StaffService staffService;
    @Resource
    private OrgService orgService;
    @Resource
    RoleService roleService;

    /**
     * 用户登录
     * @param name 用户名
     * @param password 密码
     * @return
     */
    @ApiOperation(value = "用户登录", notes="通过用户名和密码登录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= String.class)
    })
    @GetMapping("login")
    @ResponseBody
    public R login(
            @ApiParam(value="用户名",required = true)@RequestParam(name = "name") String name,
            @ApiParam(value="密码",required = true)@RequestParam(name = "password") String password) {
        Criteria criteria = Criteria.where("name").is(name);
        List<Staff> staffList = staffService.findByCond(criteria);
        String msg = "";
        if (staffList.size() > 0) {
            Staff staff = staffList.get(0);
            if (staff.getPassword().equals(password)) {
                return R.success(staff);
            } else {
                msg = "密码错误";
            }
        } else {
            msg = "用户名不存在";
        }
        return R.success(msg);
    }


    /**
     * 验证用户名唯一性
     * @param name
     * @return
     */
    @ApiOperation(value = "验证用户名唯一性", notes="检验用户名是否唯一")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= String.class)
    })
    @GetMapping("name")
    @ResponseBody
    public R name(@ApiParam(value="用户名",required = true)@RequestParam(name = "name") String name) {
        Criteria criteria = Criteria.where("name").is(name);
        List<Staff> staffList = staffService.findByCond(criteria);
        String msg = "";
        if (staffList.size() > 0) {
            msg = "用户名已存在";
        } else {
            msg = "";
        }
        return R.success(msg);
    }


    /**
     * 更新用户
     * @param staff 用户实体
     * @return
     */
    @ApiOperation(value = "更新用户", notes="更新用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Staff.class)
    })
    @PostMapping("update")
    @ResponseBody
    public R update(@ApiParam(value="用户实体",required = true)@RequestBody Staff staff) {
        staffService.update(staff);
        return R.success(staffService.find(staff.getId()));
    }


    /**
     * 新增用户
     * @param staff 用户实体
     * @return
     */
    @ApiOperation(value = "新增用户", notes="新增一条用户记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Staff.class)
    })
    @PostMapping("add")
    @ResponseBody
    public R add(@ApiParam(value="用户实体",required = true)@RequestBody Staff staff) {
        return R.success(staffService.insert(staff));
    }


    /**
     * 删除用户
     * @param id 用户id
     */
    @ApiOperation(value = "删除用户", notes="根据id删除用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @DeleteMapping("delete")
    @ResponseBody
    public void delete(@ApiParam(value="用户id",required = true)@RequestParam(name = "id") String id) {
        staffService.delete(id);
    }


    /**
     * 删除测试
     */
    @ApiOperation(value = "删除测试", notes="用于测试删除角色为超级管理员的角色")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @DeleteMapping("deleteTest")
    @ResponseBody
    public void deleteTest() {
        Criteria criteria = Criteria.where("role").is(new ObjectId("5e987108e0dfa40ea76f664a"));
        staffService.delete(criteria);
    }

    /**
     * 更新密码
     * @param id 用户id
     * @param password 新密码
     */
    @ApiOperation(value = "更新密码", notes="用于更新用户密码")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @GetMapping("password")
    @ResponseBody
    public void password(
            @ApiParam(value="用户id",required = true)@RequestParam(name = "id") String id,
            @ApiParam(value="新密码",required = true)@RequestParam(name = "password") String password) {
        staffService.update(id, "password", password);
    }

    /**
     * 用户分页
     * @param page 页数
     * @param rows 行数
     * @param cond 条件
     * @return
     */
    @ApiOperation(value = "用户分页", notes="用户列表分页")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Map.class)
    })
    @GetMapping("getPage")
    @ResponseBody
    public R getStaffPage(
            @ApiParam(value="页数")@RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @ApiParam(value="行数") @RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
            @ApiParam(value="条件")@RequestParam(required = false, name = "cond") String cond) {
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        if (parse != null) {
            if (parse.get("name") != null) {
                Pattern pattern = Pattern.compile("^.*" + parse.get("name") + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
                criteria.and("name").regex(pattern);
            }
            if (parse.get("org") != null) {
                criteria.and("org").is(new ObjectId((String) parse.get("org")));
            }
            if (parse.get("role") != null) {
                criteria.and("role").is(new ObjectId((String) parse.get("role")));
            }
            if (parse.get("state") != null) {
                criteria.and("state").is(parse.get("state"));
            }
        }
        List<Map> list = staffService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, staffService.getCount(criteria), page, rows);
        return R.success(result);
    }


    /**
     * 获取机构下某角色的用户
     * @param org 机构id
     * @param role 角色id
     * @return
     */
    @ApiOperation(value = "获取机构下某角色的用户", notes="获取机构下某特点角色的用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Staff[].class)
    })
    @GetMapping("getOrgRole")
    @ResponseBody
    public R<List<Staff>> getOrgRole(
            @ApiParam(value="机构id",required = true)@RequestParam(name = "org", required = false) String org,
            @ApiParam(value="角色id")@RequestParam(required = false, name = "role") String role) {
        Criteria criteria =new Criteria();
        criteria.and("state").is("2");
        if(null != org){
            criteria.and("org").is(new ObjectId(org));
        }
        if(null != role){
            List<Role> roleList= roleService.findByCond(Criteria.where("name").is(role));
            if(null!=roleList&&roleList.size()>0){
                criteria.and("role").is(new ObjectId(roleList.get(0).getId()));
            }
        }
        Query query = new Query(criteria);
        query.fields().include("id");
        query.fields().include("name");
        query.fields().include("realName");
        query.fields().include("_id");
        return  R.success(staffService.findByCond(query));
    }


    /**
     * 用户分析
     * @param cond 条件
     * @return
     */
    @ApiOperation(value = "用户分析", notes="用户图表分析")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Map.class)
    })
    @GetMapping("chart")
    @ResponseBody
    public R<Map<String, Object>> chart( @ApiParam(value="条件")@RequestParam(name = "cond",required =false) String cond){
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


    /**
     * 随机生成用户
     * @param num 数量
     */
    @ApiOperation(value = "随机生成用户", notes="随机生成相应数量的用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @GetMapping("random")
    @ResponseBody
    public void random(@ApiParam(value="数量")@RequestParam(name = "num",required = false,defaultValue = "10")int  num){
        List<Org> orgList = orgService.findAll();
        List<Role> roleList = roleService.findAllDefault();
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

    /**
     * 发送邮箱验证码
     * @param id 用户id
     * @param operation 操作
     * @return
     */
    @ApiOperation(value = "发送邮箱验证码", notes="发送邮箱验证码")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @GetMapping("/sendVerification")
    @ResponseBody
    public R sendVerification(
            @ApiParam(value="用户id")@RequestParam(name = "id")String id,
            @ApiParam(value="操作")@RequestParam(required = false,name = "operation") String operation) {
        if(null==staffService.find(id).getEmail()){
            return R.failed("用户邮箱不存在");
        }
        return staffService.sendVerification(id,operation);
    }

    /**
     * 通过邮箱验证码修改密码
     * @param newPassword
     * @param userId
     * @param code
     * @param operation
     * @return
     */
    @ApiOperation(value = "通过邮箱验证码修改密码", notes="通过邮箱验证码修改密码")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @GetMapping("/updatePWByVerificationCode")
    @ResponseBody
    public R updatePWByVerificationCode(
            @ApiParam(value="用户id")@RequestParam(name = "id")String id,
            @ApiParam(value="新密码")@RequestParam(name = "password")String password,
            @ApiParam(value="邮箱验证码")@RequestParam(name = "code")String code,
            @ApiParam(value="操作")@RequestParam(name = "operation")String operation
    ) {
        return this.staffService.updatePWByVerificationCode(id, password,code,operation);
    }

    /**
     * 根据用户名和邮箱查询用户
     *
     * @param name 用户名
     * @param email 邮箱
     * @return 单条数据
     */
    @ApiOperation(value = "根据用户名和邮箱查询用户", notes="根据用户名和邮箱查询用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @ResponseBody
    @GetMapping("/nameEmail")
    public R queryByUserNameAndEmail(
            @ApiParam(value="用户名")@RequestParam("name") String name,
            @ApiParam(value="邮箱")@RequestParam("email") String email) {
        Criteria criteria = Criteria.where("name").is(name);
        criteria.and("email").is(email);
        List<Staff> staffList = staffService.findByCond(criteria);
        if(null!=staffList&&staffList.size()>0){
            return R.success(staffList.get(0));
        }
        return R.failed("请输入正确的用户名和绑定的邮箱");
    }

    /**
     * 验证码验证
     * @param email
     * @param code
     * @param operation
     * @return
     */
    @ApiOperation(value = "验证码验证", notes="验证码验证")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @ResponseBody
    @GetMapping("/verificationCheck")
    public R verificationCheck(
            @ApiParam(value="邮箱")@RequestParam("email") String email,
            @ApiParam(value="验证码")@RequestParam("code") String code,
            @ApiParam(value="操作")@RequestParam(required = false) String operation) {
        return staffService.verificationCheck(email,code, operation);
    }
}
