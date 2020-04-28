package com.qing.tea.controller;

import com.google.code.kaptcha.Producer;
import com.qing.tea.entity.Tea;
import com.qing.tea.service.TeaService;
import com.qing.tea.utils.R;
import com.qing.tea.utils.VerifyUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("tea")
public class TeaController {
    @Resource
    private TeaService teaService;

    @RequestMapping("getAll")
    @ResponseBody
    public R getAll(){
        return R.success(teaService.findAll().get(0));
    }

    @RequestMapping("getPlant")
    @ResponseBody
    public R getPlant(@RequestParam(name = "page")int page, @RequestParam(name = "rows")int rows,
                      @RequestParam(name = "userId")String userId,@RequestParam(required =false,name = "finish")boolean finish){
        Criteria criteria = Criteria.where("plant.planter").is(userId);
        criteria.and("plant.finish").is(finish);
        List<Tea> list = teaService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, teaService.getCount(criteria), page, rows);
        return R.success(result);
    }

    @RequestMapping("updatePlant")
    @ResponseBody
    public R updatePlant(@RequestBody Tea tea){
        teaService.update(tea.getId(),"plant",tea.getPlant());
        return R.success(teaService.find(tea.getId()));
    }
}
