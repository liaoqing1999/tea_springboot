package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.Produce;
import com.qing.tea.service.ProduceService;
import com.qing.tea.utils.R;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("produce")
public class ProduceController {
    @Resource
    ProduceService produceService;

    @RequestMapping("getOrg")
    @ResponseBody
    public R getOrgProduce(@RequestParam(name = "org") String org) {
        Criteria criteria = Criteria.where("org").is(org);
        criteria.and("state").is("2");
        List<Produce> list = produceService.findByCond(criteria);
        return R.success(list);
    }

    @RequestMapping("getPage")
    @ResponseBody
    public R getProducePage(@RequestParam(name = "page",required =false,defaultValue = "1")int page, @RequestParam(name = "rows",required =false,defaultValue ="10")int rows,@RequestParam(required =false,name = "cond")String cond) {
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        if(parse!=null) {
            if (parse.get("state") != null) {
                criteria.and("state").is(parse.get("state"));
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
    @RequestMapping("name")
    @ResponseBody
    public R name(@RequestParam(name = "name")String name,@RequestParam(name = "org")String org){
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
    @RequestMapping("update")
    @ResponseBody
    public R update(@RequestBody Produce produce) {
        produceService.update(produce);
        return R.success(produceService.find(produce.getId()));
    }
    @RequestMapping("add")
    @ResponseBody
    public R add(@RequestBody Produce produce) {
        return R.success(produceService.insert(produce));
    }

    @RequestMapping("delete")
    @ResponseBody
    public void delete(@RequestParam(name = "id")String  id){
        produceService.delete(id);
    }
}
