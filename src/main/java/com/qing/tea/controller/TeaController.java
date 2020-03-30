package com.qing.tea.controller;

import com.mongodb.client.MongoCursor;
import com.qing.tea.utils.DBMethod;
import com.qing.tea.utils.R;
import org.bson.Document;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tea")
public class TeaController {
    @RequestMapping("test")
    @ResponseBody
    public R register(){
        DBMethod db = new DBMethod("tea");
        MongoCursor<Document> all = db.findAll();

        return R.success(all.next());
    }
}
