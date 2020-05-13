package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.News;
import com.qing.tea.entity.NewsDetail;
import com.qing.tea.entity.Role;
import com.qing.tea.service.NewsDetailService;
import com.qing.tea.service.NewsService;
import com.qing.tea.utils.R;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("newsDetail")
public class NewsDetailController {
    @Resource
    private NewsDetailService newsDetailService;
    @Resource
    private NewsService newsService;
    @RequestMapping("/get")
    @ResponseBody
    public R getNewsDetail(@RequestParam(name = "id")String id){
        return R.success(newsDetailService.find(id));
    }
    @RequestMapping("/getUser")
    @ResponseBody
    public R getNewsDetailUser(@RequestParam(name = "user")String userId,@RequestParam(name = "news")String newsId){
        Criteria criteria = Criteria.where("user").is(new ObjectId(userId));
        criteria.and("news").is(new ObjectId(newsId));
        List<NewsDetail> newsDetails = newsDetailService.findByCond(criteria);
        if(newsDetails.size()==0){
            NewsDetail newsDetail = new NewsDetail();
            newsDetail.setDate(new Date());
            newsDetail.setNews(newsId);
            newsDetail.setState("2");
            newsDetail.setUser(userId);
            newsDetails.add(newsDetailService.insert(newsDetail));
            News news = newsService.find(newsId);
            newsService.update(newsId,"read",news.getRead()+1);
        }
        return R.success(newsDetails.get(0));
    }
    @RequestMapping("add")
    @ResponseBody
    public R<NewsDetail> add(@RequestBody NewsDetail  newsDetail){
        return R.success(newsDetailService.insert(newsDetail));
    }
    @RequestMapping("updateUser")
    @ResponseBody
    public R<NewsDetail> updateRole(@RequestBody NewsDetail  newsDetail){
        newsDetailService.update(newsDetail.getId(),"up",newsDetail.isUp());
        newsDetailService.update(newsDetail.getId(),"down",newsDetail.isDown());
        newsDetailService.update(newsDetail.getId(),"rate",newsDetail.getRate());
        return R.success(newsDetailService.find(newsDetail.getId()));
    }

    @RequestMapping("getByCond")
    @ResponseBody
    public R getListByCond(@RequestParam(required =false,name = "cond")String cond){
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        if(parse!=null) {
            if (parse.get("news") != null) {
                criteria.and("news").is(new ObjectId((String) parse.get("news")));
            }
            if (parse.get("state") != null) {
                criteria.and("state").is(parse.get("state"));
            }
        }
        return R.success(newsDetailService.findByNews(criteria));
    }
}
