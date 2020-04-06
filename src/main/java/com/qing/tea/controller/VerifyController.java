package com.qing.tea.controller;

import com.qing.tea.utils.R;
import com.qing.tea.utils.VerifyUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("verify")
public class VerifyController {
    private  HttpSession session;
    @RequestMapping("getVerify")
    @ResponseBody
    public void getVerify(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //将图片输出给浏览器
         session=request.getSession();
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
    @RequestMapping("checkVerify")
    @ResponseBody
    public R checkVerify(@RequestParam(name = "verify")String verify){
        String imageCode = (String) session.getAttribute("imageCode");
        if(imageCode.equalsIgnoreCase(verify)){
            return R.success("success");
        }else{
            return R.success("error");
        }
    }

}
