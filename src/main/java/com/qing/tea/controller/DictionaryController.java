package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.Dictionary;
import com.qing.tea.service.DictionaryService;
import com.qing.tea.utils.Node;
import com.qing.tea.utils.R;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("dictionary")
public class DictionaryController {
    @Resource
    private DictionaryService dictionaryService;

    @RequestMapping("getAll")
    @ResponseBody
    public R getAll() {
        return R.success(dictionaryService.findAll());
    }

    @RequestMapping("getByCond")
    @ResponseBody
    public R getByCond(@RequestParam(name = "typeCode") String typeCode,
                       @RequestParam(required = false, defaultValue = "", name = "valueId") String valueId) {
        return R.success(dictionaryService.findByCodeValue(typeCode, valueId,"2"));
    }
    @RequestMapping("name")
    @ResponseBody
    public R name(@RequestParam(name = "typeCode")String typeCode,@RequestParam(required = false,name = "valueId")String valueId){
        Criteria criteria = Criteria.where("type_code").is(typeCode);
        if(valueId!=null){
            criteria.and("value_id").is(valueId);
        }
        List<Dictionary> dictionaryList = dictionaryService.findByCond(criteria);
        String msg = "";
        if(dictionaryList.size()>0){
            if(valueId!=null){
                msg ="字典名已存在";
            }else{
                msg ="字典类型名已存在";
            }
        }else{
            msg ="";
        }
        return R.success(msg);
    }
    @RequestMapping("getTeaType")
    @ResponseBody
    public R getTeaType() {
        Criteria criteria = Criteria.where("type_code").is("type");
        criteria.and("state").is("2");
        List<Dictionary> clist = dictionaryService.findByCond(criteria);
        Pattern pattern = Pattern.compile("^.*00$");
        criteria.and("value_id").regex(pattern);
        List<Dictionary> list = dictionaryService.findByCond(criteria);
        List<Node> result = new ArrayList<Node>();
        for (Dictionary dict : list) {
            Node node = new Node();
            node.setId(dict.getId());
            node.setLabel(dict.getValueName());
            node.setValue(dict.getValueId());
            node.setData(dict);
            node.setDisabled(true);
            result.add(node);
        }
        for (Node node : result) {
            List<Node> children  = new ArrayList<Node>();
            for (Dictionary dict : clist) {
                String valueId = dict.getValueId();
                String s1 = valueId.substring(0, valueId.length() - 2);
                String s2 = node.getValue().substring(0, node.getValue().length()-2);
                String s3 = valueId.substring(valueId.length() - 2, valueId.length());
                if (!s3.equals("00")&& s1.equals(s2)) {
                    Node n = new Node();
                    n.setId(dict.getId());
                    n.setLabel(dict.getValueName());
                    n.setValue(dict.getValueId());
                    n.setData(dict);
                    children.add(n);
                }
            }
           if(children.size()>0){
               node.setChildren(children);
           }
        }
        return R.success(result);
    }
    @RequestMapping("getType")
    @ResponseBody
    public R getTypePage(@RequestParam(required =false,name = "cond")String cond){
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        if(parse!=null){

        }
        List<Map> list = dictionaryService.findTypeList(criteria);
        return R.success(list);
    }
    @RequestMapping("get")
    @ResponseBody
    public R getPage(@RequestParam(name = "typeCode") String typeCode,
                       @RequestParam(required = false, defaultValue = "", name = "valueId") String valueId) {
        return R.success(dictionaryService.findByCodeValue(typeCode, valueId,null));
    }
    @RequestMapping("addType")
    @ResponseBody
    public R addType(@RequestBody Dictionary dictionary){
        dictionary.setState("2");
        dictionary.setValueId("0");
        dictionary.setValueName("默认数据");
        return R.success(dictionaryService.insert(dictionary));
    }
    @RequestMapping("add")
    @ResponseBody
    public R add(@RequestBody Dictionary dictionary){
        return R.success(dictionaryService.insert(dictionary));
    }
    @RequestMapping("update")
    @ResponseBody
    public R update(@RequestBody Dictionary dictionary){
        dictionaryService.update(dictionary);
        return R.success(dictionaryService.find(dictionary.getId()));
    }
    @RequestMapping("updateType")
    @ResponseBody
    public R updateType(@RequestParam(name = "typeCode")String typeCode,@RequestParam(name = "typeName")String typeName){
        Criteria criteria = Criteria.where("type_code").is(typeCode);
        dictionaryService.updateAll(criteria,"type_name",typeName);
        return R.success("success");
    }
    @RequestMapping("delete")
    @ResponseBody
    public R delete(@RequestParam(name = "id")String id){
        dictionaryService.delete(id);
        return R.success("success");
    }
    @RequestMapping("deleteType")
    @ResponseBody
    public R deleteType(@RequestParam(name = "typeCode")String typeCode){
        Criteria criteria = Criteria.where("type_code").is(typeCode);
        dictionaryService.delete(criteria);
        return R.success("success");
    }
    @RequestMapping("getByType")
    @ResponseBody
    public R getTypeValue(@RequestParam(name = "typeCodes") String[] typeCodes) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (String typeCode : typeCodes) {
            Criteria criteria = Criteria.where("type_code").is(typeCode);
            criteria.and("state").is("2");
            List<Dictionary> list = dictionaryService.findByCond(criteria);
            result.put(typeCode, list);
        }
        return R.success(result);
    }
}
