package com.qing.tea.controller;
import com.qing.tea.service.ProduceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("produce")
public class ProduceController {
    @Resource
    ProduceService produceService;
}
