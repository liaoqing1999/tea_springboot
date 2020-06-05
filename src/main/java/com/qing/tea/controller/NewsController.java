package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.News;
import com.qing.tea.entity.NewsDetail;
import com.qing.tea.service.NewsService;
import com.qing.tea.utils.R;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


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
    public R getNewsPage(@RequestParam(name = "page")int page, @RequestParam(name = "rows")int rows,@RequestParam(required =false,name = "cond")String cond){
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        if(parse!=null) {
            if (parse.get("type") != null) {
                criteria.and("type").is(parse.get("type"));
            }
            if (parse.get("org") != null) {
                criteria.and("org").is(parse.get("org"));
            }
            if (parse.get("state") != null) {
                criteria.and("state").is(parse.get("state"));
            }
            if (parse.get("date") != null) {
                JSONArray date = (JSONArray) parse.get("date");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startDate=formatter.parse(date.get(0).toString());
                    Date endDate=formatter.parse(date.get(1).toString());
                    criteria.andOperator(
                            Criteria.where("date").gte(startDate),
                            Criteria.where("date").lte(endDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        List<News> list = newsService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, newsService.getCount(criteria), page, rows);
        return R.success(result);
    }

    @RequestMapping("delete")
    @ResponseBody
    public R delete(@RequestParam(name = "id")String id){
        newsService.delete(id);
        return R.success("");
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public R<News> updateUser(@RequestBody News  news){
        News n = newsService.find(news.getId());
        newsService.update(news.getId(),"up",n.getUp()+news.getUp());
        newsService.update(news.getId(),"down",n.getDown()+news.getDown());
        if(news.getRate()!=0){
            newsService.update(news.getId(),"rate",(n.getRate()*n.getRateNum()+news.getRate())/(n.getRateNum()+news.getRateNum()));
        }

        newsService.update(news.getId(),"rateNum",n.getRateNum()+news.getRateNum());
        return R.success(newsService.find(news.getId()));
    }

    @RequestMapping("add")
    @ResponseBody
    public R<News> add(@RequestBody News  news){
        return R.success(newsService.insert(news));
    }

    @RequestMapping("update")
    @ResponseBody
    public R<News> update(@RequestBody News  news){
        newsService.update(news);
        return R.success(newsService.find(news.getId()));
    }

    @RequestMapping("chart")
    @ResponseBody
    public R<Map<String, Object>> chart(@RequestParam(name = "cond",required =false) String cond) {
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        String str = "substr(add(date,28800000),0,10)";
        String sortName = "read";
        if(parse!=null) {
            if (parse.get("type") != null) {
                if(parse.get("type").equals("all")){
                    str =  "substr(add(date,28800000),0,4)";
                }else if(parse.get("type").equals("date")){
                    str =  "substr(add(date,28800000),11,2)";
                }else if(parse.get("type").equals("year")){
                    str =  "substr(add(date,28800000),5,2)";
                }else {
                    str =  "substr(add(date,28800000),8,2)";
                }
            }
            if (parse.get("sortName") != null) {
                sortName =(String) parse.get("sortName");
            }
            if (parse.get("dates") != null) {
                JSONArray date = (JSONArray) parse.get("dates");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startDate=formatter.parse(date.get(0).toString());
                    Date endDate=formatter.parse(date.get(1).toString());
                    criteria.andOperator(
                            Criteria.where("date").gte(startDate),
                            Criteria.where("date").lt(endDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return R.success(newsService.chart(criteria,str,sortName));
    }
}
