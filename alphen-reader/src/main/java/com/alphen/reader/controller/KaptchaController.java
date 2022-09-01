package com.alphen.reader.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class KaptchaController {
    @Resource
    private Producer kaptchaProducer;

    @GetMapping ("/verify_code")
    public void createVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //响应过期时间
        response.setDateHeader("Expires",0);
        //不缓存任何图片数据,必须重新进行校验
        response.setHeader("Cache-control","no-store,no-cache,must-revalidate");
        response.setHeader("Cache-control","post-check=0,pre-check=0");
        response.setHeader("Pragma","no-cache");
//        设置内容为图片
        response.setContentType("image/png");
//        生成验证码字符文本
        String verifyCode = kaptchaProducer.createText();
//        将字符串文本存储到Session当中
        request.getSession().setAttribute("kaptchaVerifyCode",verifyCode);
        System.out.println(request.getSession().getAttribute("kaptchaVerifyCode"));
//        创建验证码缓存图片
        BufferedImage image = kaptchaProducer.createImage(verifyCode);
//        采用二进制输出流，输出到响应界面，假如是字符，用getWriter()输出
//        将生成的image图片放入到输出流中，输出格式为png
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image,"png",out);
        out.flush();
        out.close();
    }
}
