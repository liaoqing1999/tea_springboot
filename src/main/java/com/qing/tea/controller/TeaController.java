package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.Tea;
import com.qing.tea.service.TeaService;
import com.qing.tea.utils.QRCodeUtil;
import com.qing.tea.utils.R;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("getPage")
    @ResponseBody
    public R getProducePage(@RequestParam(name = "page",required =false,defaultValue = "1")int page, @RequestParam(name = "rows",required =false,defaultValue ="10")int rows,@RequestParam(required =false,name = "cond")String cond) {
        Criteria criteria = new Criteria();
        JSONObject parse = JSON.parseObject(cond);
        if(parse!=null) {
            if (parse.get("grade") != null) {
                criteria.and("grade").is(parse.get("grade"));
            }
            if (parse.get("produce") != null) {
                criteria.and("produce").is(parse.get("produce"));
            }
        }
        List<Object> list = teaService.findListStaff(page,rows,criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, teaService.getCount(criteria), page, rows);
        return R.success(result);
    }

    @RequestMapping("add")
    @ResponseBody
    public R add(@RequestBody Tea tea){
        return R.success(teaService.insert(tea));
    }
    @RequestMapping("update")
    @ResponseBody
    public R update(@RequestBody Tea tea){
        teaService.update(tea);
        return R.success(teaService.find(tea.getId()));
    }

    @RequestMapping("delete")
    @ResponseBody
    public R delete(@RequestParam(name = "id")String id){
        teaService.delete(id);
        return R.success("");
    }
    @RequestMapping("get")
    @ResponseBody
    public R get(@RequestParam(name = "id")String id){
        return R.success(teaService.find(id));
    }

    @RequestMapping("downloadQR")
    @ResponseBody
    public void updateQr(@RequestParam(name = "id")String id,@RequestParam(name = "name")String name, HttpServletResponse response) throws Exception{
        BufferedImage image = QRCodeUtil.createImage("溯源码为：" + id, null, false);
        if (null!=image) {
            response.setContentType("image/jpeg;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition",
                    "attachment;fileName=" + URLEncoder.encode(name+".jpg", "utf-8") );// 设置文件名
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean flag = ImageIO.write(image, "jpg", out);
            byte[] buffer = out.toByteArray();
            OutputStream os = response.getOutputStream();
            os.write(buffer, 0,buffer.length);
            os.flush();
        }
    }


    @RequestMapping("getPlant")
    @ResponseBody
    public R getPlant(@RequestParam(name = "page")int page, @RequestParam(name = "rows")int rows,
                      @RequestParam(name = "userId")String userId,@RequestParam(required =false,name = "finish")boolean finish){
        Criteria criteria = Criteria.where("plant.planter").is(userId);
        criteria.and("plant.finish").is(finish);
        List<Tea> list = teaService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, teaService.getCount(criteria), page, rows);
        return R.success(result);
    }

    @RequestMapping("updatePlant")
    @ResponseBody
    public R updatePlant(@RequestBody Tea tea){
        teaService.update(tea.getId(),"plant",tea.getPlant());
        return R.success(teaService.find(tea.getId()));
    }

    @RequestMapping("getProcess")
    @ResponseBody
    public R getProcess(@RequestParam(name = "page")int page, @RequestParam(name = "rows")int rows,
                      @RequestParam(name = "userId")String userId,@RequestParam(required =false,name = "finish")boolean finish){
        Criteria criteria = Criteria.where("processFinish").is(finish);
        criteria.and("process.processer").is(userId);
        List<Tea> list = teaService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, teaService.getCount(criteria), page, rows);
        return R.success(result);
    }

    @RequestMapping("updateProcess")
    @ResponseBody
    public R updateProcess(@RequestBody Tea tea){
        teaService.update(tea.getId(),"process",tea.getProcess());
        teaService.update(tea.getId(),"processFinish",tea.isProcessFinish());
        return R.success(teaService.find(tea.getId()));
    }

    @RequestMapping("getStorage")
    @ResponseBody
    public R getStorage(@RequestParam(name = "page")int page, @RequestParam(name = "rows")int rows,
                        @RequestParam(name = "userId")String userId,@RequestParam(required =false,name = "finish")boolean finish){
        Criteria criteria = Criteria.where("storage.storageer").is(userId);
        criteria.and("storage.finish").is(finish);
        List<Tea> list = teaService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, teaService.getCount(criteria), page, rows);
        return R.success(result);
    }

    @RequestMapping("updateStorage")
    @ResponseBody
    public R updateStorage(@RequestBody Tea tea){
        teaService.update(tea.getId(),"storage",tea.getStorage());
        return R.success(teaService.find(tea.getId()));
    }

    @RequestMapping("getCheck")
    @ResponseBody
    public R getCheck(@RequestParam(name = "page")int page, @RequestParam(name = "rows")int rows,
                        @RequestParam(name = "userId")String userId,@RequestParam(required =false,name = "finish")boolean finish){
        Criteria criteria = Criteria.where("check_finish").is(finish);
        criteria.and("check.checker").is(userId);
        List<Tea> list = teaService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, teaService.getCount(criteria), page, rows);
        return R.success(result);
    }

    @RequestMapping("updateCheck")
    @ResponseBody
    public R updateCheck(@RequestBody Tea tea){
        teaService.update(tea.getId(),"check",tea.getCheck());
        teaService.update(tea.getId(),"checkFinish",tea.isCheckFinish());
        return R.success(teaService.find(tea.getId()));
    }
}
