package com.qing.tea.controller;

import com.qing.tea.entity.News;
import com.qing.tea.entity.NewsDetail;
import com.qing.tea.entity.Role;
import com.qing.tea.service.NewsDetailService;
import com.qing.tea.service.NewsService;
import com.qing.tea.utils.R;
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
    public R getNewsDetailUser(@RequestParam(name = "userId")String userId,@RequestParam(name = "newsId")String newsId){
        Criteria criteria = Criteria.where("userId").is(userId);
        criteria.and("newsId").is(newsId);
        List<NewsDetail> newsDetails = newsDetailService.findByCond(criteria);
        if(newsDetails.size()==0){
            NewsDetail newsDetail = new NewsDetail();
            newsDetail.setDate(new Date());
            newsDetail.setNewsId(newsId);
            newsDetail.setState("2");
            newsDetail.setUserId(userId);
            newsDetails.add(newsDetailService.insert(newsDetail));
            News news = newsService.find(newsId);
            newsService.update(newsId,"read",news.getRead()+1);
        }
        return R.success(newsDetails);
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
}
