package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.Produce;
import com.qing.tea.service.ProduceService;
import com.qing.tea.utils.R;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
@Api(tags  = "产品操作接口")
@RestController
@RequestMapping("produce")
public class ProduceController {
    /**
     * 服务对象
     */
    @Resource
    ProduceService produceService;

    /**
     * 获取机构下的产品
     * @param org 机构id
     * @return
     */
    @ApiOperation(value = "获取机构下的产品", notes="通过机构id获取机构下的产品")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Produce[].class)
    })
    @GetMapping("getOrg")
    @ResponseBody
    public R getOrgProduce(@ApiParam(value="机构id",required = true)@RequestParam(name = "org") String org) {
        Criteria criteria = Criteria.where("org").is(new ObjectId(org));
        criteria.and("state").is("2");
        List<Produce> list = produceService.findByCond(criteria);
        return R.success(list);
    }

    /**
     * 分页获取产品
     * @param page 页数
     * @param rows 行数
     * @param cond 条件
     * @return
     */
    @ApiOperation(value = "分页获取产品", notes="分页获取产品")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Map.class)
    })
    @GetMapping("getPage")
    @ResponseBody
    public R getProducePage(
            @ApiParam(value="页数")@RequestParam(name = "page",required =false,defaultValue = "1")int page,
            @ApiParam(value="行数")@RequestParam(name = "rows",required =false,defaultValue ="10")int rows,
            @ApiParam(value="条件")@RequestParam(required =false,name = "cond")String cond) {
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        if(parse!=null) {
            if (parse.get("state") != null) {
                criteria.and("state").is(parse.get("state"));
            }
            if(parse.get("name")!=null){
                Pattern pattern = Pattern.compile("^.*" + parse.get("name") + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
                criteria.and("name").regex(pattern);
            }
            if (parse.get("org") != null) {
                criteria.and("org").is(new ObjectId(parse.get("org").toString()));
            }
            if (parse.get("grade") != null) {
                criteria.and("grade").is(parse.get("grade"));
            }
            if (parse.get("typeId") != null) {
                criteria.and("type_id").is(parse.get("typeId"));
            }
        }
        List<Map> list = produceService.findList(page,rows,criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, produceService.getCount(criteria), page, rows);
        return R.success(result);
    }

    /**
     * 获取相似产品
     * @param typeId 类型
     * @param org 机构
     * @return
     */
    @ApiOperation(value = "获取相似产品", notes="根据类型和机构获取相似产品")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Map[].class)
    })
    @GetMapping("getLike")
    @ResponseBody
    public R getLike(
            @ApiParam(value="类型")@RequestParam(required =false,name = "typeId")String typeId,
            @ApiParam(value="机构")@RequestParam(required =false,name = "org")String org) {
        Criteria criteria = new Criteria();
        criteria.and("state").is("2");
        Criteria c = new Criteria();
        if(typeId!=null){
            c.orOperator(Criteria.where("type_id").is(typeId));
        }
        if(org!=null){
            c.orOperator(Criteria.where("org").is(new ObjectId(org)));
        }
        c.andOperator(criteria);
        List<Map> list = produceService.findList(1,4,criteria);
        return R.success(list);
    }

    /**
     * 验证产品名唯一性
     * @param name 产品名
     * @param org 机构
     * @return
     */
    @ApiOperation(value = "验证产品名唯一性", notes="验证该机构下产品名的唯一性")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= String.class)
    })
    @GetMapping("name")
    @ResponseBody
    public R name(
            @ApiParam(value="产品名",required = true)@RequestParam(name = "name")String name,
            @ApiParam(value="机构",required = true)@RequestParam(name = "org")String org){
        Criteria criteria = Criteria.where("name").is(name);
        criteria.and("org").is(new ObjectId(org));
        List<Produce> produceList = produceService.findByCond(criteria);
        String msg = "";
        if(produceList.size()>0){
            msg ="机构下产品名已存在";
        }else{
            msg ="";
        }
        return R.success(msg);
    }

    /**
     * 更新产品
     * @param produce 产品实体
     * @return
     */
    @ApiOperation(value = "更新产品", notes="更新产品")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Produce.class)
    })
    @PostMapping("update")
    @ResponseBody
    public R update(@ApiParam(value="产品实体",required = true)@RequestBody Produce produce) {
        produceService.update(produce);
        return R.success(produceService.find(produce.getId()));
    }

    /**
     * 新增产品
     * @param produce 产品实体
     * @return
     */
    @ApiOperation(value = "新增产品", notes="新增一条产品记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Produce.class)
    })
    @PostMapping("add")
    @ResponseBody
    public R add(@ApiParam(value="产品实体",required = true)@RequestBody Produce produce) {
        return R.success(produceService.insert(produce));
    }

    /**
     * 删除产品
     * @param id 产品id
     */
    @ApiOperation(value = "删除产品", notes="根据id删除产品")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @DeleteMapping("delete")
    @ResponseBody
    public void delete(@ApiParam(value="产品id",required = true)@RequestParam(name = "id")String  id){
        produceService.delete(id);
    }


    /**
     * 产品分析
     * @param cond 条件
     * @return
     */
    @ApiOperation(value = "产品分析", notes="产品图表分析")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Map.class)
    })
    @GetMapping("chart")
    @ResponseBody
    public R<Map<String, Object>> chart(
            @ApiParam(value="条件") @RequestParam(name = "cond",required =false) String cond) {
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        String str = "substr(type_id,0,4)";
        if(parse!=null) {
            if(parse.get("typeId")!=null){
                str =  "substr(type_id,0,6)";
                Pattern pattern = Pattern.compile("^.*" + parse.get("typeId") + ".*$");//这里时使用的是正则匹配,searchKey是关键字，接口传参，也可以自己定义。
                criteria.and("type_id").regex(pattern);
            }
        }
        return R.success(produceService.chart(criteria,str));
    }
}
