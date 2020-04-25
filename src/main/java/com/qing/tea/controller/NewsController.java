package com.qing.tea.controller;

import com.qing.tea.entity.News;
import com.qing.tea.entity.NewsDetail;
import com.qing.tea.service.NewsService;
import com.qing.tea.utils.R;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("news")
public class NewsController {
    @Resource
    private NewsService newsService;

    @RequestMapping("/getNews")
    @ResponseBody
    public R getNews(@RequestParam(name = "id")String id){
        return R.success(newsService.find(id));
    }

    @RequestMapping("getPage")
    @ResponseBody
    public R getRolePage(@RequestParam(name = "page")int page, @RequestParam(name = "rows")int rows,@RequestParam(required =false,defaultValue="",name = "type")String type){
        Criteria criteria = new Criteria();
        if(!type.equals("")&&type!=null){
            criteria.and("type").is(type);
        }
        List<News> list = newsService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, newsService.getCount(criteria), page, rows);
        return R.success(result);
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public R<News> updateRole(@RequestBody News  news){
        News n = newsService.find(news.getId());
        newsService.update(news.getId(),"up",n.getUp()+news.getUp());
        newsService.update(news.getId(),"down",n.getDown()+news.getDown());
        newsService.update(news.getId(),"rate",(n.getRate()*n.getRateNum()+news.getRate())/(n.getRateNum()+news.getRateNum()));
        newsService.update(news.getId(),"rateNum",n.getRateNum()+news.getRateNum());
        return R.success(newsService.find(news.getId()));
    }
}
