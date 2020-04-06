package com.qing.tea.controller;

import com.google.code.kaptcha.Producer;
import com.qing.tea.service.TeaService;
import com.qing.tea.utils.R;
import com.qing.tea.utils.VerifyUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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


}
