package com.qing.tea.controller;

import com.qing.tea.entity.Dictionary;
import com.qing.tea.service.DictionaryService;
import com.qing.tea.utils.R;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dictionary")
public class DictionaryController {
    @Resource
    private DictionaryService dictionaryService;

    @RequestMapping("getAll")
    @ResponseBody
    public R getAll(){
        return R.success(dictionaryService.findAll());
    }

    @RequestMapping("getByCond")
    @ResponseBody
    public R getByCond(@RequestParam(name = "typeCode")String typeCode, @RequestParam(required =false,defaultValue="",name = "valueId")String valueId){
        return R.success( dictionaryService.findByCodeValue(typeCode,valueId));
    }

    @RequestMapping("getByType")
    @ResponseBody
    public R getTypeValue(@RequestParam(name = "typeCodes")String[] typeCodes){
        Map<String,Object> result =new HashMap<String,Object>();
        for(String typeCode : typeCodes){
            Criteria criteria = Criteria.where("type_code").is(typeCode);
            List<Dictionary> list = dictionaryService.findByCond(criteria);
            result.put(typeCode,list);
        }
        return R.success( result);
    }
}
