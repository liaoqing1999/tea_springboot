package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.News;
import com.qing.tea.entity.NewsDetail;
import com.qing.tea.service.NewsDetailService;
import com.qing.tea.service.NewsService;
import com.qing.tea.utils.R;
import io.swagger.annotations.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Api(tags  = "用户资讯记录操作接口")
@RestController
@RequestMapping("newsDetail")
public class NewsDetailController {
    /**
     * 服务对象
     */
    @Resource
    private NewsDetailService newsDetailService;
    @Resource
    private NewsService newsService;

    /**
     * 获取用户资讯记录
     * @param id
     * @return
     */
    @ApiOperation(value = "获取用户资讯记录", notes="根据id获取用户资讯记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= NewsDetail.class)
    })
    @GetMapping("/get")
    @ResponseBody
    public R getNewsDetail(@ApiParam(value="用户资讯记录id",required = true)@RequestParam(name = "id")String id){
        return R.success(newsDetailService.find(id));
    }

    /**
     * 获取和用户相关的资讯记录
     * @param userId 用户id
     * @param newsId 资讯id
     * @return
     */
    @ApiOperation(value = "获取用户特定资讯记录", notes="根据用户id和资讯id获取和用户相关的资讯记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= NewsDetail.class)
    })
    @GetMapping("/getUser")
    @ResponseBody
    public R getNewsDetailUser(
            @ApiParam(value="用户id",required = true)@RequestParam(name = "user")String userId,
            @ApiParam(value="资讯id",required = true)@RequestParam(name = "news")String newsId){
        Criteria criteria = Criteria.where("user").is(new ObjectId(userId));
        criteria.and("news").is(new ObjectId(newsId));
        List<NewsDetail> newsDetails = newsDetailService.findByCond(criteria);
        if(null==newsDetails||newsDetails.size()==0){
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

    /**
     * 新增用户资讯记录
     * @param newsDetail 资讯实体
     * @return
     */
    @ApiOperation(value = "新增用户资讯记录", notes="新增一条用户资讯记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= NewsDetail.class)
    })
    @PostMapping("add")
    @ResponseBody
    public R<NewsDetail> add( @ApiParam(value="资讯实体",required = true)@RequestBody NewsDetail  newsDetail){
        return R.success(newsDetailService.insert(newsDetail));
    }

    /**
     * 更新用户资讯记录
     * @param newsDetail 资讯实体
     * @return
     */
    @ApiOperation(value = "更新特定用户资讯记录", notes="用户操作资讯后更新资讯记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= NewsDetail.class)
    })
    @PostMapping("updateUser")
    @ResponseBody
    public R<NewsDetail> updateRole(@ApiParam(value="资讯实体",required = true)@RequestBody NewsDetail  newsDetail){
        newsDetailService.update(newsDetail.getId(),"up",newsDetail.isUp());
        newsDetailService.update(newsDetail.getId(),"down",newsDetail.isDown());
        newsDetailService.update(newsDetail.getId(),"rate",newsDetail.getRate());
        return R.success(newsDetailService.find(newsDetail.getId()));
    }

    /**
     * 通过条件查询用户资讯记录
     * @param cond 条件
     * @return
     */
    @ApiOperation(value = "根据条件查询用户资讯记录", notes="根据条件查询用户资讯记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= NewsDetail[].class)
    })
    @GetMapping("getByCond")
    @ResponseBody
    public R getListByCond(@ApiParam(value="条件")@RequestParam(required =false,name = "cond")String cond){
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
