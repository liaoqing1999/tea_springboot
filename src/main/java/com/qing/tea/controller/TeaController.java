package com.qing.tea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.tea.entity.Tea;
import com.qing.tea.service.TeaService;
import com.qing.tea.utils.QRCodeUtil;
import com.qing.tea.utils.R;
import io.swagger.annotations.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Api(tags  = "茶叶操作接口")
@RestController
@RequestMapping("tea")
public class TeaController {
    /**
     * 服务对象
     */
    @Resource
    private TeaService teaService;

    /**
     * 获取所有茶叶
     * @return
     */
    @ApiOperation(value = "获取所有茶叶", notes="获取所有茶叶")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response= Tea[].class)
    })
    @GetMapping("getAll")
    @ResponseBody
    public R getAll(){
        return R.success(teaService.findAll().get(0));
    }

    /**
     * 分页获取茶叶
     * @param page 页数
     * @param rows 行数
     * @param cond 条件
     * @return
     */
    @ApiOperation(value = "分页获取茶叶", notes="分页获取茶叶")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Map.class)
    })
    @GetMapping("getPage")
    @ResponseBody
    public R getProducePage(
            @ApiParam(value="页数")@RequestParam(name = "page",required =false,defaultValue = "1")int page,
            @ApiParam(value="行数")@RequestParam(name = "rows",required =false,defaultValue ="10")int rows,
            @ApiParam(value="条件")@RequestParam(required =false,name = "cond")String cond) {
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

    /**
     * 新增茶叶
     * @param tea 茶叶实体
     * @return
     */
    @ApiOperation(value = "新增茶叶", notes="新增一条茶叶记录")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Tea.class)
    })
    @PostMapping("add")
    @ResponseBody
    public R add(@ApiParam(value="茶叶实体",required = true)@RequestBody Tea tea){
        return R.success(teaService.insert(tea));
    }

    /**
     * 更新茶叶
     * @param tea
     * @return
     */
    @ApiOperation(value = "更新茶叶", notes="更新茶叶")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Tea.class)
    })
    @PostMapping("update")
    @ResponseBody
    public R update(@ApiParam(value="茶叶实体",required = true)@RequestBody Tea tea){
        teaService.update(tea);
        return R.success(teaService.find(tea.getId()));
    }


    /**
     * 删除茶叶
     * @param id 茶叶id
     * @return
     */
    @ApiOperation(value = "删除茶叶", notes="根据id删除茶叶")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=String.class)
    })
    @DeleteMapping("delete")
    @ResponseBody
    public R delete(@ApiParam(value="茶叶id",required = true)@RequestParam(name = "id")String id){
        teaService.delete(id);
        return R.success("");
    }

    /**
     * 获取茶叶
     * @param id 茶叶id
     * @return
     */
    @ApiOperation(value = "获取茶叶", notes="根据id获取茶叶")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response=Tea.class)
    })
    @GetMapping("get")
    @ResponseBody
    public R get(@ApiParam(value="茶叶id",required = true)@RequestParam(name = "id")String id){
        return R.success(teaService.find(id));
    }


    /**
     * 生成并下载二维码
     * @param id 茶叶id
     * @param name 下载名
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "生成并下载二维码", notes="生成并下载二维码")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @GetMapping("downloadQR")
    @ResponseBody
    public void updateQr(
            @ApiParam(value="茶叶id",required = true) @RequestParam(name = "id")String id,
            @ApiParam(value="下载名",required = true) @RequestParam(name = "name")String name,
            HttpServletResponse response) throws Exception{
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

    /**
     * 分页获取种植阶段属于该用户的茶叶
     * @param page 页数
     * @param rows 行数
     * @param userId 用户id
     * @param finish 是否已完成
     * @return
     */
    @ApiOperation(value = "分页获取种植阶段属于该用户的茶叶", notes="分页获取种植阶段属于该用户的茶叶")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Map.class)
    })
    @GetMapping("getPlant")
    @ResponseBody
    public R getPlant(
            @ApiParam(value="页数")@RequestParam(name = "page",defaultValue = "1",required = false)int page,
            @ApiParam(value="行数")@RequestParam(name = "rows",defaultValue = "3",required = false)int rows,
            @ApiParam(value="用户id",required = true)@RequestParam(name = "userId")String userId,
            @ApiParam(value="是否已完成")@RequestParam(required =false,name = "finish")boolean finish){
        Criteria criteria = Criteria.where("plant.planter").is(userId);
        criteria.and("plant.finish").is(finish);
        List<Tea> list = teaService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, teaService.getCount(criteria), page, rows);
        return R.success(result);
    }


    /**
     * 更新种植阶段
     * @param tea 茶叶实体
     * @return
     */
    @ApiOperation(value = "更新种植阶段", notes="更新种植阶段")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Tea.class)
    })
    @PostMapping("updatePlant")
    @ResponseBody
    public R updatePlant(@ApiParam(value="茶叶实体",required = true)@RequestBody Tea tea){
        teaService.update(tea.getId(),"plant",tea.getPlant());
        return R.success(teaService.find(tea.getId()));
    }


    /**
     * 分页获取加工阶段属于该用户的茶叶
     * @param page 页数
     * @param rows 行数
     * @param userId 用户id
     * @param finish 是否已完成
     * @return
     */
    @ApiOperation(value = "分页获取加工阶段属于该用户的茶叶", notes="分页获取加工阶段属于该用户的茶叶")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Map.class)
    })
    @GetMapping("getProcess")
    @ResponseBody
    public R getProcess(
            @ApiParam(value="页数")@RequestParam(name = "page",defaultValue = "1",required = false)int page,
            @ApiParam(value="行数")@RequestParam(name = "rows",defaultValue = "3",required = false)int rows,
            @ApiParam(value="用户id",required = true)@RequestParam(name = "userId")String userId,
            @ApiParam(value="是否已完成")@RequestParam(required =false,name = "finish")boolean finish){
        Criteria criteria = Criteria.where("processFinish").is(finish);
        criteria.and("process.processer").is(userId);
        List<Tea> list = teaService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, teaService.getCount(criteria), page, rows);
        return R.success(result);
    }


    /**
     * 更新加工阶段
     * @param tea 茶叶实体
     * @return
     */
    @ApiOperation(value = "更新加工阶段", notes="更新加工阶段")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Tea.class)
    })
    @PostMapping("updateProcess")
    @ResponseBody
    public R updateProcess(@ApiParam(value="茶叶实体",required = true)@RequestBody Tea tea){
        teaService.update(tea.getId(),"process",tea.getProcess());
        teaService.update(tea.getId(),"processFinish",tea.isProcessFinish());
        return R.success(teaService.find(tea.getId()));
    }


    /**
     * 分页获取仓储阶段属于该用户的茶叶
     * @param page 页数
     * @param rows 行数
     * @param userId 用户id
     * @param finish 是否已完成
     * @return
     */
    @ApiOperation(value = "分页获取仓储阶段属于该用户的茶叶", notes="分页获取仓储阶段属于该用户的茶叶")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Map.class)
    })
    @GetMapping("getStorage")
    @ResponseBody
    public R getStorage( @ApiParam(value="页数")@RequestParam(name = "page",defaultValue = "1",required = false)int page,
                         @ApiParam(value="行数")@RequestParam(name = "rows",defaultValue = "3",required = false)int rows,
                         @ApiParam(value="用户id",required = true)@RequestParam(name = "userId")String userId,
                         @ApiParam(value="是否已完成")@RequestParam(required =false,name = "finish")boolean finish){
        Criteria criteria = Criteria.where("storage.storageer").is(userId);
        criteria.and("storage.finish").is(finish);
        List<Tea> list = teaService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, teaService.getCount(criteria), page, rows);
        return R.success(result);
    }


    /**
     * 更新仓储阶段
      * @param tea 茶叶实体
     * @return
     */
    @ApiOperation(value = "更新仓储阶段", notes="更新仓储阶段")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Tea.class)
    })
    @PostMapping("updateStorage")
    @ResponseBody
    public R updateStorage(@ApiParam(value="茶叶实体",required = true)@RequestBody Tea tea){
        teaService.update(tea.getId(),"storage",tea.getStorage());
        return R.success(teaService.find(tea.getId()));
    }


    /**
     * 分页获取检测阶段属于该用户的茶叶
     * @param page 页数
     * @param rows 行数
     * @param userId 用户id
     * @param finish 是否已完成
     * @return
     */
    @ApiOperation(value = "分页获取检测阶段属于该用户的茶叶", notes="分页获取检测阶段属于该用户的茶叶")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Map.class)
    })
    @GetMapping("getCheck")
    @ResponseBody
    public R getCheck( @ApiParam(value="页数")@RequestParam(name = "page",defaultValue = "1",required = false)int page,
                       @ApiParam(value="行数")@RequestParam(name = "rows",defaultValue = "3",required = false)int rows,
                       @ApiParam(value="用户id",required = true)@RequestParam(name = "userId")String userId,
                       @ApiParam(value="是否已完成")@RequestParam(required =false,name = "finish")boolean finish){
        Criteria criteria = Criteria.where("check_finish").is(finish);
        criteria.and("check.checker").is(userId);
        List<Tea> list = teaService.findList(page, rows, criteria);
        Map<String, Object> result = MapReuslt.mapPage(list, teaService.getCount(criteria), page, rows);
        return R.success(result);
    }


    /**
     * 更新检测阶段
     * @param tea 茶叶实体
     * @return
     */
    @ApiOperation(value = "更新检测阶段", notes="更新检测阶段")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功",response = Tea.class)
    })
    @PostMapping("updateCheck")
    @ResponseBody
    public R updateCheck(@ApiParam(value="茶叶实体",required = true)@RequestBody Tea tea){
        teaService.update(tea.getId(),"check",tea.getCheck());
        teaService.update(tea.getId(),"checkFinish",tea.isCheckFinish());
        return R.success(teaService.find(tea.getId()));
    }
}
