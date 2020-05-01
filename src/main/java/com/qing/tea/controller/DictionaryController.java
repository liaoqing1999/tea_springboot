package com.qing.tea.controller;

import com.qing.tea.entity.Dictionary;
import com.qing.tea.service.DictionaryService;
import com.qing.tea.utils.Node;
import com.qing.tea.utils.R;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public R getByCond(@RequestParam(name = "typeCode") String typeCode, @RequestParam(required = false, defaultValue = "", name = "valueId") String valueId) {
        return R.success(dictionaryService.findByCodeValue(typeCode, valueId));
    }

    @RequestMapping("getType")
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
