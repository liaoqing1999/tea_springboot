package com.qing.tea.controller;

import com.qing.tea.entity.Dictionary;
import com.qing.tea.entity.Produce;
import com.qing.tea.service.ProduceService;
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
}
