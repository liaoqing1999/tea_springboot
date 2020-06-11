package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.Dictionary;
import com.qing.tea.service.DictionaryService;
import com.qing.tea.utils.Node;
import com.qing.tea.utils.R;
import io.swagger.annotations.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
@Api(tags  = "字典操作接口")
@RestController
@RequestMapping("dictionary")
public class DictionaryController {

    /**
     * 服务层对象
     */
    @Resource
    private DictionaryService dictionaryService;

    /**
     * 获取所有字典
     * @return
     */
    @GetMapping("getAll")
    @ApiOperation(value = "获取所有字典", notes="返回所有字段的集合")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Dictionary[].class)
    })
    @ResponseBody
    public R<List<Dictionary>> getAll() {
        return R.success(dictionaryService.findAll());
    }

    /**
     * 通过条件获取字典
     * @param typeCode 类型code
     * @param valueId  值id
     * @return
     */
    @ApiOperation(value = "通过条件获取字典", notes="根据类型code和值id返回对应的字典数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Dictionary[].class)
    })
    @GetMapping("getByCond")
    @ResponseBody
    public R<List<Dictionary>> getByCond(@ApiParam(value="类型code",required = true) @RequestParam(name = "typeCode") String typeCode,
                                         @ApiParam(value="值id")@RequestParam(required = false, defaultValue = "", name = "valueId") String valueId) {
        return R.success(dictionaryService.findByCodeValue(typeCode, valueId,"2"));
    }

    /**
     * 字典类型名和字典值唯一性校验
     * @param typeCode 类型code
     * @param valueId 值id
     * @return
     */
    @ApiOperation(value = "字典类型名和字典值唯一性校验", notes="根据类型code和值id验证唯一性")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=String.class)
    })
    @GetMapping("name")
    @ResponseBody
    public R name(@ApiParam(value="类型code",required = true) @RequestParam(name = "typeCode") String typeCode,
                  @ApiParam(value="值id")@RequestParam(required = false, defaultValue = "", name = "valueId") String valueId){
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

    /**
     * 获取茶叶类型
     * @return
     */
    @ApiOperation(value = "获取茶叶类型", notes="返回茶叶类型树")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Node[].class)
    })
    @GetMapping("getTeaType")
    @ResponseBody
    public R<List<Node>> getTeaType() {
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

    /**
     * 获取字典类型List
     * @param cond 条件
     * @return
     */
    @ApiOperation(value = "获取全部字典类型", notes="返回字典类型List")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Map[].class)
    })
    @GetMapping("getType")
    @ResponseBody
    public R<List<Map>> getTypePage( @ApiParam(value="条件") @RequestParam(required =false,name = "cond")String cond){
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        if(parse!=null){

        }
        List<Map> list = dictionaryService.findTypeList(criteria);
        return R.success(list);
    }

    /**
     * 获取字典List
     * @param typeCode 类型Code
     * @param valueId 值id
     * @return
     */
    @ApiOperation(value = "获取字典List", notes="根据字典类型返回字典List")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Dictionary[].class)
    })
    @GetMapping("get")
    @ResponseBody
    public R<List<Dictionary>> getPage(@ApiParam(value="类型Code",required = true)@RequestParam(name = "typeCode") String typeCode,
                                       @ApiParam(value="值id") @RequestParam(required = false, defaultValue = "", name = "valueId") String valueId) {
        return R.success(dictionaryService.findByCodeValue(typeCode, valueId,null));
    }

    /**
     * 新增字典类型
     * @param dictionary 字典实体
     * @return
     */
    @ApiOperation(value = "新增字典类型", notes="新增一个字典类型，带有一条默认数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Dictionary.class)
    })
    @PostMapping("addType")
    @ResponseBody
    public R<Dictionary> addType(@ApiParam(value="字典实体" ,required = true)@RequestBody Dictionary dictionary){
        dictionary.setState("2");
        dictionary.setValueId("0");
        dictionary.setValueName("默认数据");
        return R.success(dictionaryService.insert(dictionary));
    }

    /**
     * 新增字典
     * @param dictionary 字典实体
     * @return
     */
    @ApiOperation(value = "新增字典", notes="新增字典")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Dictionary.class)
    })
    @PostMapping("add")
    @ResponseBody
    public R<Dictionary> add(@ApiParam(value="字典实体" ,required = true)@RequestBody Dictionary dictionary){
        return R.success(dictionaryService.insert(dictionary));
    }

    /**
     * 更新字典
     * @param dictionary 字典实体
     * @return
     */
    @ApiOperation(value = "更新字典", notes="更新字典")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Dictionary.class)
    })
    @PostMapping("update")
    @ResponseBody
    public R<Dictionary> update(@ApiParam(value="字典实体",required = true)@RequestBody Dictionary dictionary){
        dictionaryService.update(dictionary);
        return R.success(dictionaryService.find(dictionary.getId()));
    }

    /**
     * 更新字典类型
     * @param typeCode 类型Code
     * @param typeName 类型名
     * @return
     */
    @ApiOperation(value = "更新字典类型", notes="更新字典类型，并更新所有该类型的字典数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=String.class)
    })
    @GetMapping("updateType")
    @ResponseBody
    public R<String> updateType(@ApiParam(value="类型Code",required = true)@RequestParam(name = "typeCode")String typeCode,
                                @ApiParam(value="类型名",required = true)@RequestParam(name = "typeName")String typeName){
        Criteria criteria = Criteria.where("type_code").is(typeCode);
        dictionaryService.updateAll(criteria,"type_name",typeName);
        return R.success("success");
    }

    /**
     * 删除字典
     * @param id 字典id
     * @return
     */
    @ApiOperation(value = "删除字典", notes="根据id删除字典")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=String.class)
    })
    @DeleteMapping("delete")
    @ResponseBody
    public R delete( @ApiParam(value="字典id",required = true) @RequestParam(name = "id")String id){
        dictionaryService.delete(id);
        return R.success("success");
    }

    /**
     * 删除字典类型
     * @param typeCode 类型Code
     * @return
     */
    @ApiOperation(value = "删除字典类型", notes="根据类型Code删除字典类型，并删除该类型下所有字典数据")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=String.class)
    })
    @DeleteMapping("deleteType")
    @ResponseBody
    public R deleteType(@ApiParam(value="类型Code",required = true)@RequestParam(name = "typeCode")String typeCode){
        Criteria criteria = Criteria.where("type_code").is(typeCode);
        dictionaryService.delete(criteria);
        return R.success("success");
    }


    /**
     * 获取字典集合
     * @param typeCodes 类型Code数组
     * @return
     */
    @ApiOperation(value = "获取字典集合", notes="根据类型Code数组获取类型下字典数据，并返回map对应字典集合")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Map.class)
    })
    @GetMapping("getByType")
    @ResponseBody
    public R<Map<String, Object>> getTypeValue(@ApiParam(value="类型Code数组",required = true)@RequestParam(name = "typeCodes") String[] typeCodes) {
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
