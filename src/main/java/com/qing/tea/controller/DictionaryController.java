package com.qing.tea.controller;

import com.qing.tea.service.DictionaryService;
import com.qing.tea.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    public R getByCond(@RequestParam(name = "typeCode")String typeCode, @RequestParam(name = "valueId")String valueId){

        return R.success( dictionaryService.findByCodeValue(typeCode,valueId));
    }
}
