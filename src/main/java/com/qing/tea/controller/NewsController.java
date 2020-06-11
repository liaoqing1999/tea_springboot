package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.News;
import com.qing.tea.service.NewsService;
import com.qing.tea.utils.R;
import io.swagger.annotations.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags  = "资讯操作接口")
@RestController
@RequestMapping("news")
public class NewsController {

    /**
     * 服务层对象
     */
    @Resource
    private NewsService newsService;

    /**
     * 获取资讯
     * @param id 资讯id
     * @return
     */
    @ApiOperation(value = "获取资讯", notes="根据id获取资讯")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= News.class)
    })
    @GetMapping("/getNews")
    @ResponseBody
    public R<News> getNews(@ApiParam(value="资讯id",required = true)@RequestParam(name = "id")String id){
        return R.success(newsService.find(id));
    }

    /**
     * 分页获取资讯
     * @param page 页数
     * @param rows 行数
     * @param cond 条件
     * @return
     */
    @ApiOperation(value = "获取资讯", notes="根据id获取资讯")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Map.class)
    })
    @GetMapping("getPage")
    @ResponseBody
    public R<Map<String, Object>> getNewsPage(@ApiParam(value="页数")@RequestParam(name = "page", required = false, defaultValue = "1")int page,
                                              @ApiParam(value="行数")@RequestParam(name = "rows", required = false, defaultValue = "3")int rows,
                                              @ApiParam(value="条件")@RequestParam(required =false,name = "cond")String cond){
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


    /**
     * 删除资讯
     * @param id 资讯id
     * @return
     */
    @ApiOperation(value = "删除资讯", notes="根据id删除资讯")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=String.class)
    })
    @DeleteMapping("delete")
    @ResponseBody
    public R delete(@ApiParam(value="资讯id",required = true)@RequestParam(name = "id")String id){
        newsService.delete(id);
        return R.success("");
    }

    /***
     * 用户观看资讯后更新资讯
     * @param news 资讯实体
     * @return
     */
    @ApiOperation(value = "更新用户资讯", notes="用户观看资讯后更新资讯")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=News.class)
    })
    @PostMapping("updateUser")
    @ResponseBody
    public R<News> updateUser(@ApiParam(value="资讯实体",required = true)@RequestBody News  news){
        News n = newsService.find(news.getId());
        newsService.update(news.getId(),"up",n.getUp()+news.getUp());
        newsService.update(news.getId(),"down",n.getDown()+news.getDown());
        if(news.getRate()!=0){
            newsService.update(news.getId(),"rate",(n.getRate()*n.getRateNum()+news.getRate())/(n.getRateNum()+news.getRateNum()));
        }

        newsService.update(news.getId(),"rateNum",n.getRateNum()+news.getRateNum());
        return R.success(newsService.find(news.getId()));
    }

    /**
     * 新增资讯
     * @param news 资讯实体
     * @return
     */
    @ApiOperation(value = "新增资讯", notes="新增一条资讯记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=News.class)
    })
    @PostMapping("add")
    @ResponseBody
    public R<News> add(@ApiParam(value="资讯实体",required = true)@RequestBody News  news){
        return R.success(newsService.insert(news));
    }


    /**
     * 更新资讯
     * @param news 资讯实体
     * @return
     */
    @ApiOperation(value = "更新资讯", notes="更新一条资讯")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=News.class)
    })
    @PostMapping("update")
    @ResponseBody
    public R<News> update(@ApiParam(value="资讯实体",required = true)@RequestBody News  news){
        newsService.update(news);
        return R.success(newsService.find(news.getId()));
    }


    /**
     * 资讯分析
     * @param cond 条件
     * @return
     */
    @ApiOperation(value = "资讯分析", notes="资讯图表分析")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Map.class)
    })
    @GetMapping("chart")
    @ResponseBody
    public R<Map<String, Object>> chart(@ApiParam(value="资讯实体")@RequestParam(name = "cond",required =false) String cond) {
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
