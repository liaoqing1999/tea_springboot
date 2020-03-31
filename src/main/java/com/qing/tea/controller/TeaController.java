package com.qing.tea.controller;

import com.qing.tea.service.impl.TeaServiceImpl;
import com.qing.tea.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("tea")
public class TeaController {
    @Resource
    private TeaServiceImpl teaService;

    @RequestMapping("getAll")
    @ResponseBody
    public R register(){
        return R.success(teaService.findAll());
    }
}
