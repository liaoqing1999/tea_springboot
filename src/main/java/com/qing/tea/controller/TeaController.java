package com.qing.tea.controller;

import com.google.code.kaptcha.Producer;
import com.qing.tea.service.TeaService;
import com.qing.tea.utils.R;
import com.qing.tea.utils.VerifyUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("tea")
public class TeaController {
    @Resource
    private TeaService teaService;
    @Resource
    private Producer kaptchaProducer;
    @RequestMapping("getAll")
    @ResponseBody
    public R getAll(){
        return R.success(teaService.findAll().get(0));
    }

    @RequestMapping("test")
    @ResponseBody
    public void test(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //将图片输出给浏览器
        HttpSession session=request.getSession();

        //利用图片工具生成图片
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyUtil.createImage();
        //将验证码存入Session
        session.setAttribute("imageCode",objs[0]);
        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }

}
